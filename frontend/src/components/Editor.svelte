<script lang="ts">
    import loader from "@monaco-editor/loader";
    import { onDestroy, onMount } from "svelte";
    import type * as Monaco from "monaco-editor/esm/vs/editor/editor.api";
    import { title, viewingScript, visitedTabs } from "../stores";
    import type { Script } from "$lib/models/models";
    import { copyArr } from "$lib/util/copyArr";
    import { saveFile } from "$lib/util/storeManagement/saveFile";
    import { syncScript } from "$lib/util/storeManagement/syncScript";
    import { ScriptService } from "$lib/util/api/script/ScriptService";
    import { get } from "svelte/store";
    import { ToastManager } from "$lib/util/toast/ToastManager";

    let editor: Monaco.editor.IStandaloneCodeEditor;
    let monaco: typeof Monaco;
    let editorContainer: HTMLElement;

    syncScript.set(
        async () => {
            const script = await ScriptService
                .findByName(
                    get(viewingScript)!!.fileName
                )
            editor.getModel()?.setValue(script.fileContent)

            ToastManager.dispatch("Sync", `Pulled latest script content for ${get(viewingScript)!!.fileName}`, "info")
        }
    )

    onMount(async () => {
        // Remove the next two lines to load the monaco editor from a CDN
        // see https://www.npmjs.com/package/@monaco-editor/loader#config
        const monacoEditor = await import("monaco-editor");
        loader.config({monaco: monacoEditor.default});

        monaco = await loader.init();

        // Your monaco instance is ready, let's display some code!
        editor = monaco.editor.create(editorContainer, {
            glyphMargin: true,
            theme: "vs-dark",
            lightbulb: {
                enabled: true
            }
        });

        let saveTimeOut = false
        let syncTimeOut = false

        editor.addCommand(
            monaco.KeyMod.CtrlCmd | monaco.KeyCode.KeyS,
            () => {
                if (saveTimeOut) {
                    return
                }

                saveFile();
                saveTimeOut = true

                setTimeout(() => {
                    saveTimeOut = false
                }, 250)
            }
        );

        editor.addCommand(
            monaco.KeyMod.CtrlCmd | monaco.KeyCode.KeyB,
            () => {
                if (syncTimeOut) {
                    return
                }

                $syncScript();
                syncTimeOut = true

                setTimeout(() => {
                    syncTimeOut = false
                }, 250)
            }
        );

        viewingScript.subscribe((script: any) => {
            editor.getModel()?.dispose()

            if (script != null) {
                const model = monaco.editor.createModel(
                    script.fileContent,
                    "typescript",
                    undefined
                );
                editor.setModel(model)

                const backingScript = $viewingScript

                if (backingScript != null) {
                    const _script = backingScript as Script
                    const fileName = _script.fileName as string

                    title.set(`Editing ${fileName}...`)
                    visitedTabs.set(
                        copyArr($visitedTabs, fileName)
                    )
                }
            } else {
                title.set("Home")
            }
        })

        editor.onDidChangeModelContent(() => {
            const backingScript = $viewingScript

            if (backingScript != null) {
                const script = backingScript as Script
                script.fileContent = editor.getValue()
            }
        })
    });

    onDestroy(() => {
        monaco?.editor.getModels().forEach((model) => model.dispose());
    });
</script>

<section class="h-screen" bind:this={editorContainer}/>
