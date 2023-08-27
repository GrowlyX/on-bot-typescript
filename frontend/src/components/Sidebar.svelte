<script lang="ts">
    import FileTree from "./FileDisplay/FileTree.svelte";
    import Runner from "./Runner.svelte";
    import {files} from "../stores";
</script>

<aside class="h-screen bg-zinc-900 drop-shadow-xl">
    <FileTree files={$files}/>

    <Runner/>
</aside>
