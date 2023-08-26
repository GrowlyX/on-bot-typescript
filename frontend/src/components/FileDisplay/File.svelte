<script lang="ts">
    import {findScript, findScriptByName} from "$lib/util/api/script/findScript";
    import {viewingScript} from "../../stores";
    import type {Script} from "$lib/models/models";

    export let name: string;

    // TODO: not integrated properly in the frontend
    export function isViewingScript(): boolean {
        if ($viewingScript != null) {
            if ($viewingScript.fileName == name) {
                return true
            }
        }

        return false
    }

    export async function handleClick() {
        if ($viewingScript != null) {
            if ($viewingScript.fileName == name) {
                return
            }
        }

        const script = await findScriptByName(name)
        viewingScript.set(script)
    }
</script>

<!-- svelte-ignore a11y-missing-attribute -->
<li><a on:click|preventDefault={handleClick} aria-pressed={isViewingScript()}>{name}</a></li>
