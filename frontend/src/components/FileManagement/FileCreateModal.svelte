<script lang="ts">
    import { createScript } from "$lib/util/api/script/createScript";

    let name = "example.kts"

    async function createFile() {
        if (name.includes(' ') || !name.endsWith(".kts")) {
            activateToast(`The script name "${name}" is invalid!`, "failure bg-red-600 text-white")
            return
        }

        try {
            await createScript({name})
            await refreshFileList();

            activateToast("Your script was created!", "success bg-green-500 text-black")
        } catch (error: any) {
            activateToast(error.error, "failure bg-red-600 text-white")
        }
    }
</script>

<dialog id="fileCreateModal" class="modal">
    <form on:submit={createFile} method="dialog" class="modal-box w-96 flex justify-center">
        <div class="form-control w-full">
            <span class="label label-text">Enter a filename: <span class="italic text-gray-500">enter to submit</span></span>
            <input type="text" bind:value={name} class="input input-bordered w-full"/>
            <input class="hidden" type="submit"/>
        </div>
    </form>

    <form method="dialog" class="modal-backdrop">
        <button>close</button>
    </form>
</dialog>
