<script lang="ts">
    import {pathsToTree} from "$lib/files/pathsToTree";
    import {files, viewingScript} from "../../stores";
    import type {TFile} from "$lib/files/TFile";
    import {writable} from "svelte/store";
    import Folder from "./Folder.svelte";

    let root = writable<TFile[]>([]);

    files.subscribe((files: string[]) => {
        root.set(
            pathsToTree(files)
                .sort((n1: TFile, n2: TFile) =>
                    n2.files.length - n1.files.length
                )
        )
    })
</script>

<section>
    <div class="flex justify-center">
        <img on:click={() => { viewingScript.set(null) }}  class="cursor-pointer active:bg-slate-700 rounded-md my-1" src="logo.png" id="logo" alt="OnBot Kotlin Logo">
    </div>

    <ul class="p-5 m-6 rounded-md menu bg-zinc-700">
        <Folder name="" files={$root} isRoot />
    </ul>
</section>

<style>
    #logo {
        width: 20rem;
    }
</style>
