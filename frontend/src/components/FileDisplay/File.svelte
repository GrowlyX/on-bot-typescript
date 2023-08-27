<script lang="ts">
    import {findScriptByName} from "$lib/util/api/script/findScript";
    import {viewingScript} from "../../stores";

    export let name: string;
    export let path: string;

    // TODO: not integrated properly in the frontend
    export function isViewingScript(): boolean {
        if ($viewingScript != null) {
            if ($viewingScript.fileName == path) {
                return true
            }
        }

        return false
    }

    export async function handleClick() {
        const script = await findScriptByName(path)
        viewingScript.set(script)
    }
</script>

<!-- svelte-ignore a11y-missing-attribute -->
<li>
    <a class="icon octicon-file kotlin-icon" on:click|preventDefault={handleClick} aria-pressed={isViewingScript()}>{name}</a>
</li>
