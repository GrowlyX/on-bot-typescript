<script lang="ts">
    import { caretToPosition } from "$lib/util/caretToPosition"
    import { ToastManager } from "$lib/util/toast/ToastManager"
    import { onMount } from "svelte"
    import { ScriptService } from "$lib/util/api/script/ScriptService";
    import { refreshFileList } from "$lib/util/storeManagement/refreshFileList";

    let name = ".kts"

    const init = () => {
        name = ".kts"
        caretToPosition("name", 0)
    }

    const createFile = async () => {
        if (/\s+/.test(name) || !name.endsWith(".kts")) {
            return ToastManager.dispatch(`"${name}" is not a valid script name.`, "failure")
        }

        try {
            await ScriptService.create({ fileName: name })
            await refreshFileList()

            ToastManager.dispatch("Script created.", "success")
        } catch (error: any) {
            ToastManager.dispatch(error.error, "failure")
        } finally {
            init()
        }
    }

    // move caret to the start of the input
    onMount(() => {
        init()
    })
</script>

<dialog id="fileCreateModal" class="modal">
    <form on:submit={createFile} method="dialog" class="modal-box w-96 flex justify-center">
        <div class="form-control w-full">
            <span class="label label-text">
                Enter a filename: <span class="italic text-gray-500">enter to submit</span>
            </span>
            <input type="text" id="name" bind:value={name} class="input input-bordered w-full" />
            <input class="hidden" type="submit" />
        </div>
    </form>

    <form method="dialog" class="modal-backdrop">
        <button>close</button>
    </form>
</dialog>
