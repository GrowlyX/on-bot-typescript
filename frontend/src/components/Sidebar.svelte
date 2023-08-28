<script lang="ts">
    import FileTree from "./FileDisplay/FileTree.svelte";
    import Runner from "./Runner.svelte";
    import { files, viewingScript } from "../stores";
    import { goto } from "$app/navigation";
</script>

<aside class="flex flex-col h-screen bg-[#121212] drop-shadow-xl">
    <FileTree files={$files}/>

    <Runner/>
</aside>
