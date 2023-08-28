<script lang="ts">
    import { apiStatus, files, getScriptNames, viewingScript, visitedTabs } from "../stores";
    import { updateScriptContent } from "$lib/util/api/script/updateScriptContent";
    import { deleteScriptByName } from "$lib/util/api/script/deleteScript";
    import { createScript } from "$lib/util/api/script/createScript";
    import { copyAndRemoveValue } from "$lib/util/copyArr";
    import { findScriptByName } from "$lib/util/api/script/findScript";

    let fileName = "example.kts"
    let deleteFileConfirm = ""

    let toastActive = false
    let toastText = ""
    let toastStatus = ""
    let activeToastTimeout = -1

    async function refreshFileList() {
        files.set(await getScriptNames());
    }

    function activateToast(text: string, status: string) {
        toastText = text
        toastStatus = status
        toastActive = true

        if (activeToastTimeout !== -1) {
            clearTimeout(activeToastTimeout)
        }

        activeToastTimeout = setTimeout(() => {
            toastActive = false
        }, 3000)
    }

    async function syncScript() {
        const script = await findScriptByName($viewingScript?.fileName!!)
        viewingScript.set(script)

        activateToast("Script was synced", "info bg-blue-500 text-white")
    }

    async function deleteFile() {
        if (deleteFileConfirm !== "confirm") {
            return
        }

        await deleteScriptByName($viewingScript?.fileName!!);
        await refreshFileList();

        visitedTabs.set(
            copyAndRemoveValue($visitedTabs, $viewingScript?.fileName!!)
        )

        // reset the viewing script to dispose of current model
        viewingScript.set(null);
        activateToast("Script was deleted!", "failure bg-red-600 text-white")
    }

    async function saveFile() {
        await updateScriptContent($viewingScript!!)
        activateToast("Script was saved!", "success bg-green-500 text-black")
    }

    async function createFile() {
        if (fileName.includes(' ') || !fileName.endsWith(".kts")) {
            activateToast(`The script name "${fileName}" is invalid!`, "failure bg-red-600 text-white")
            return
        }

        try {
            await createScript({fileName})
            await refreshFileList();

            activateToast("Your script was created!", "success bg-green-500 text-black")
        } catch (error: any) {
            activateToast(error.error, "failure bg-red-600 text-white")
        }
    }

</script>

<section class="flex justify-center p-5">
    {#if toastActive}
        <div class="toast toast-start">
            <div class="alert alert-{toastStatus}">
                <span>{toastText}</span>
            </div>
        </div>
    {/if}

    {#if $viewingScript !== null}
        <dialog id="confirmDeleteModal" class="modal">
            <form on:submit={deleteFile} method="dialog" class="modal-box">
                <div class="form-control w-full max-w-xs">
                    <span class="label label-text">Enter "confirm" to delete the the script "{$viewingScript.fileName}":</span>
                    <input type="text" id="{$viewingScript.fileName}" bind:value={deleteFileConfirm}
                           class="input input-bordered w-full max-w-xs"/>
                    <input class="hidden" type="submit"/>
                </div>
            </form>

            <form method="dialog" class="modal-backdrop">x
                <button>close</button>
            </form>
        </dialog>

        <div class="justify-center w-full join">
            <button on:click={saveFile} data-tip="save script"
                    class="tooltip w-[70%] btn bg-[#1ed760] text-black hover:bg-[#17b34f] join-item">
                Save
            </button>
            <button on:click={syncScript} data-tip="sync script"
                    class="tooltip w-[15%] btn bg-blue-500 hover:bg-blue-700 text-gray-100 join-item">
                <!-- TODO: icon :) -->
                🔄
            </button>

            <button onclick="confirmDeleteModal.showModal()" data-tip="delete script"
                    class="tooltip w-[15%] btn bg-red-500 hover:bg-red-700 text-gray-100 join-item">
                <!-- TODO: icon :) -->
                🗑️
            </button>
        </div>
    {:else}
        <div class="justify-center w-full join">
            <button class="w-[100%] btn bg-[#1ed760] text-black hover:bg-[#1ed760] hover:scale-[102%] join-item"
                    onclick="fileCreateModal.showModal()">Create a script
            </button>
        </div>

        <dialog id="fileCreateModal" class="modal">
            <form on:submit={createFile} method="dialog" class="modal-box">
                <div class="form-control w-full max-w-xs">
                    <span class="label label-text">Enter a filename:</span>
                    <input type="text" bind:value={fileName} class="input input-bordered w-full max-w-xs"/>
                    <input class="hidden" type="submit"/>
                </div>
            </form>

            <form method="dialog" class="modal-backdrop">
                <button>close</button>
            </form>
        </dialog>
    {/if}
</section>
