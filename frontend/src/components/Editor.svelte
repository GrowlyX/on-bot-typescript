<script lang="ts">
    import loader from "@monaco-editor/loader";
    import { onDestroy, onMount } from "svelte";
    import type * as Monaco from "monaco-editor/esm/vs/editor/editor.api";
    import { title, viewingScript, visitedTabs } from "../stores";
    import type {Script} from "$lib/models/models";
    import { copyArr } from "$lib/util/copyArr";

    let editor: Monaco.editor.IStandaloneCodeEditor;
    let monaco: typeof Monaco;
    let editorContainer: HTMLElement;

    onMount(async () => {
        // Remove the next two lines to load the monaco editor from a CDN
        // see https://www.npmjs.com/package/@monaco-editor/loader#config
        const monacoEditor = await import("monaco-editor");
        loader.config({ monaco: monacoEditor.default });

        monaco = await loader.init();

        // Your monaco instance is ready, let's display some code!
        editor = monaco.editor.create(editorContainer);
        monaco.editor.setTheme("vs-dark");

        viewingScript.subscribe((script: any) => {
            editor.getModel()?.dispose()

            if (script != null) {
                const model = monaco.editor.createModel(
                    script.fileContent,
                    "kotlin",
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

<section class="h-screen" bind:this={editorContainer} />
