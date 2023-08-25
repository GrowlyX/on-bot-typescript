<script lang="ts">
    import { isFile } from "$lib/files/isFile";
    import { merge, pathsToTree } from "$lib/files/pathsToTree";
    import Folder from "./Folder.svelte";

    export let files: string[];

    let root = pathsToTree(files);
</script>

<ul class="p-5 m-5 rounded-md menu bg-zinc-700">
    <!-- TODO: map on each node of `root` instead of one big root element because that's scuffed -->

    <Folder name="ratio + didn't ask + cope" files={root} isRoot />
</ul>
