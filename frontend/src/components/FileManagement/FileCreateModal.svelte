<script lang="ts">
    import { caretToPosition } from "$lib/util/caretToPosition"
    import { ToastManager } from "$lib/util/toast/ToastManager"
    import { onMount } from "svelte"
    import { ScriptService } from "$lib/util/api/script/ScriptService";
    import { refreshFileList } from "$lib/util/storeManagement/refreshFileList";

    let name = ""

    type Failure = {
        error: string
        enabled: boolean
    }

    let failure: Failure = {
        error: "",
        enabled: false
    }

    async function validateScriptName() {
        if (name.length === 0) {
            failure = {
                error: "Script name cannot be empty!",
                enabled: true
            }
            return
        }

        if (/\s+/.test(name)) {
            failure = {
                error: "Script name must not contain whitespaces!",
                enabled: true
            }
            return
        }

        try {
            await ScriptService.findByName(`${name}.kts`)

            failure = {
                error: "Script already exists!",
                enabled: true
            }
            return
        } catch (ignored) {

        }

        failure = {
            error: "",
            enabled: false
        }
    }

    const createFile = async () => {
        if (failure.enabled) {
            return
        }

        try {
            await ScriptService.create({ fileName: `${name}.kts` })
            await refreshFileList()

            ToastManager.dispatch("Script created.", "success")
        } catch (error: any) {
            ToastManager.dispatch(`Couldn't submit script create for ${error.error}`, "failure")
        }
    }
</script>

<dialog id="fileCreateModal" class="modal">
    <form on:submit|preventDefault={createFile} on:keyup={validateScriptName} method="dialog" class="modal-box w-96 flex justify-center">
        <div class="form-control w-full">
            <span class="label label-text">
                Enter a filename: <span class="italic text-gray-500">enter to submit</span>
            </span>

            <input type="text" id="name" bind:value={name} class={(failure.enabled ? "border-red-500 " : "") +  "input input-bordered w-full"} />

            {#if failure.enabled}
                <span class="label label-text text-red-500">{failure.error}</span>
            {/if}

            <input class="hidden" type="submit" />
        </div>
    </form>

    <form method="dialog" class="modal-backdrop">
        <button>close</button>
    </form>
</dialog>
