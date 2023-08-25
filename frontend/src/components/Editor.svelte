<script lang="ts">
    import loader from "@monaco-editor/loader";
    import { onDestroy, onMount } from "svelte";
    import type * as Monaco from "monaco-editor/esm/vs/editor/editor.api";

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
        const editor = monaco.editor.create(editorContainer);

        monaco.editor.setTheme("vs-dark");

        const model = monaco.editor.createModel(
            `logger.log("Starting script")

val leftDrive = hardwareMap.get(DcMotorEx::class.java, "leftDrive")
val rightDrive = hardwareMap.get(DcMotorEx::class.java, "rightDrive")

while (!isStopRequested) {
    val drive = -gamepad1.left_stick_y.toDouble()
    val turn = gamepad1.right_stick_x.toDouble()

    val leftPower = Range.clip(drive + turn, -1.0, 1.0)
    val rightPower = Range.clip(drive - turn, -1.0, 1.0)

    // Tank Mode uses one stick to control each wheel.
    // - This requires no math, but it is hard to drive forward slowly and keep straight.
    // leftPower  = -gamepad1.left_stick_y ;
    // rightPower = -gamepad1.right_stick_y ;

    // Send calculated power to wheels
    leftDrive.power = leftPower
    rightDrive.power = rightPower
}`,
            "kotlin",
            monaco.Uri.file("sample.kt")
        );
        editor.setModel(model);
    });

    onDestroy(() => {
        monaco?.editor.getModels().forEach((model) => model.dispose());
    });
</script>

<section class="h-screen" bind:this={editorContainer} />
