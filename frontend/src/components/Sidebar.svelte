<script lang="ts">
    import FileTree from "./FileDisplay/FileTree.svelte";
    import Runner from "./Island.svelte";
    import { files } from "../stores";
    import Logo from "./Logo/Logo.svelte";
</script>

<aside class="flex flex-col h-screen bg-[#121212] drop-shadow-xl">
    <Logo/>
    <FileTree files={$files}/>
    <Runner/>
</aside>
