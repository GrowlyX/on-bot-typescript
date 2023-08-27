<script lang="ts">
    import {pathsToTree} from "$lib/files/pathsToTree";
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
    <Folder name="" files={$root} isRoot />
</ul>
