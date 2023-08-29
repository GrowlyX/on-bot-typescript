<script lang="ts">
    import { viewingScript } from "../../stores";
    import { ScriptService } from "$lib/util/api/script/ScriptService";

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
        const script = await ScriptService.findByName(path)
        viewingScript.set(script)
    }
</script>

<!-- svelte-ignore a11y-missing-attribute -->
<!-- svelte-ignore a11y-no-static-element-interactions -->
<!-- svelte-ignore a11y-click-events-have-key-events -->
<li>
    {#if currentlyViewing}
        <a class="icon octicon-file kotlin-icon hover:text-white">{name}</a>
    {:else}
        <a class="icon octicon-file kotlin-icon hover:text-white" on:click|preventDefault={handleClick}>{name}</a>
    {/if}
</li>
