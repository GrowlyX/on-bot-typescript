<script lang="ts">
    import FileTree from "./FileDisplay/FileTree.svelte";
    import Runner from "./Runner.svelte";
    import {files} from "../stores";
</script>

<aside>
    <FileTree files={$files}/>

    <Runner/>
</aside>
