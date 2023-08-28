<script lang="ts">
    import type { TFile } from "$lib/files/TFile";
    import { isFile } from "$lib/files/isFile";
    import File from "./File.svelte";

    export let files: TFile[];
    export let name: string;
    export let isRoot: boolean;
</script>

<!-- svelte-ignore a11y-click-events-have-key-events -->
<!-- svelte-ignore a11y-no-noninteractive-element-interactions -->
{#if isRoot}
    <ul class="hover:text-white">
        {#each files as file}
            {#if isFile(file.name)}
                <File {...file}/>
            {:else}
                <li>
                    <svelte:self {...file}/>
                </li>
            {/if}
        {/each}
    </ul>
{:else}
    <details class="hover:text-white">
        <summary>
            {name}
        </summary>
        <ul>
            {#each files as file}
                {#if isFile(file.name)}
                    <File {...file}/>
                {:else}
                    <li>
                        <svelte:self {...file}/>
                    </li>
                {/if}
            {/each}
        </ul>
    </details>
{/if}
