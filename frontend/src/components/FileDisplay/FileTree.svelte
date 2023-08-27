<script lang="ts">
    import {isFile} from "$lib/files/isFile";
    import {merge, pathsToTree} from "$lib/files/pathsToTree";
    import Folder from "./Folder.svelte";
    import {files} from "../../stores";
    import type {TFile} from "$lib/files/TFile";
    import {writable} from "svelte/store";

    let root = writable<TFile[]>([]);

    files.subscribe((files: string[]) => {
        root.set(pathsToTree(files))
    })
</script>

<ul class="p-5 m-5 rounded-md menu bg-zinc-700">
    <h1 class="text-center font-bold text-white">Your Scripts:</h1>
    <!-- TODO: map on each node of `root` instead of one big root element because that's scuffed -->

    <Folder name="ratio + didn't ask + cope" files={$root} isRoot />
</ul>
