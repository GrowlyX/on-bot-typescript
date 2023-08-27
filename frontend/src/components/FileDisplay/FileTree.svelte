<script lang="ts">
    import { pathsToTree } from "$lib/files/pathsToTree";
    import { files, viewingScript } from "../../stores";
    import type { TFile } from "$lib/files/TFile";
    import { writable } from "svelte/store";
    import Folder from "./Folder.svelte";

    let root = writable<TFile[]>([]);

    files.subscribe((files: string[]) => {
        root.set(
            pathsToTree(files)
        )
    })
</script>

<section>
    <div on:click={() => { viewingScript.set(null) }} class="flex justify-center hover:drop-shadow-xl">
        <img class="cursor-pointer hover:bg-zinc-800 rounded-md my-1 scale-75"
             src="logo.png" id="logo" alt="OnBot Kotlin Logo">
    </div>

    <ul class="p-5 m-6 rounded-md menu bg-zinc-800 text-slate-200 hover:text-white">
        {#if $root.length === 0}
            <div class="flex justify-center">
                <span class="loading loading-spinner loading-xs p-3" ></span>
                <div class="pl-2">Loading scripts...</div>
            </div>
        {:else}
            <Folder name="" files={$root} isRoot />
        {/if}
    </ul>
</section>

<style>
    #logo {
        width: 30rem;
    }
</style>
