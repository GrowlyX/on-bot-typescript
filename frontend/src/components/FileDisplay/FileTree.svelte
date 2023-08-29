<script lang="ts">
    import { pathsToTree } from "$lib/files/pathsToTree";
    import { fileListLoaded, files, viewingScript } from "../../stores";
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
    <div on:click={() => { viewingScript.set(null) }} class="flex flex-col justify-center hover:drop-shadow-xl">
        <img class="cursor-pointer hover:bg-[#2a2a2a] rounded-md scale-75"
             src="logo.png" id="logo" alt="OnBot Kotlin Logo">
    </div>

    <ul class="p-3 m-3 rounded-md menu bg-[#181818] text-white">
        {#if $fileListLoaded === false}
            <div class="flex justify-center">
                <span class="loading loading-spinner loading-xs p-3"></span>
                <div class="pl-2">Loading scripts...</div>
            </div>
        {:else}
            {#if $root.length !== 0}
                <Folder name="" files={$root} isRoot/>
            {:else}
                <div class="flex justify-center">
                    ⚠️
                    <div class="pl-2">No scripts found!</div>
                </div>
            {/if}
        {/if}
    </ul>
</section>

<style>
    #logo {
        width: 30rem;
    }
</style>
