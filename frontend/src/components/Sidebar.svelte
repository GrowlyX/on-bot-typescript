<script>
    import FileTree from "./FileDisplay/FileTree.svelte";
    import Runner from "./Runner.svelte";

    const files = [
        "dev/DevTeleOp.kts",
        "dev/DevAutonomous.kts",
        "prod/ProdTeleOp.kts",
        "prod/ProdAutonomous.kts",
        "shared.kts",
    ]
</script>

<aside>
    <FileTree {files}/>

    <Runner/>
</aside>