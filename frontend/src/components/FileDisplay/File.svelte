<script lang="ts">
    import { findScriptByName } from "$lib/util/api/script/findScript";
    import { viewingScript } from "../../stores";
    import MetaTitle from "../Meta/MetaTitle.svelte";

    export let name: string;
    export let path: string;

    let currentlyViewing = false

    viewingScript.subscribe((script) => {
        if (script != null) {
            if (script.fileName == path) {
                currentlyViewing = true
                return
            }
        }

        currentlyViewing = false
    })

    export async function handleClick() {
        const script = await findScriptByName(path)
        viewingScript.set(script)
    }
</script>

<!-- svelte-ignore a11y-missing-attribute -->
<li>
    {#if currentlyViewing}
        <a class="icon octicon-file kotlin-icon hover:text-white">{name}</a>
    {:else}
        <a class="icon octicon-file kotlin-icon hover:text-white" on:click|preventDefault={handleClick}>{name}</a>
    {/if}
</li>
