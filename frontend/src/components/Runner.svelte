<script lang="ts">
    import { refreshFileList } from "$lib/util/storeManagement/refreshFileList"
    import { deleteScriptByName } from "$lib/util/api/script/deleteScript"
    import { copyAndRemoveValue } from "$lib/util/copyArr"
    import { viewingScript, visitedTabs } from "../stores"

    import FileCreateModal from "./FileManagement/FileCreateModal.svelte"
    import { syncScript } from "$lib/util/storeManagement/syncScript"
    import { saveFile } from "$lib/util/storeManagement/saveFile"

    let deleteFileConfirm = ""

    let toastActive = false
    let toastText = ""
    let toastStatus = ""
    let activeToastTimeout = -1 

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
    
    async function deleteFile() {
        if (deleteFileConfirm !== "confirm") {
            return
        }

        await deleteScriptByName($viewingScript?.fileName!!)
        await refreshFileList()

        visitedTabs.set(copyAndRemoveValue($visitedTabs, $viewingScript?.fileName!!))

        // reset the viewing script to dispose of current model
        viewingScript.set(null)
        activateToast("Script was deleted!", "failure bg-red-600 text-white")
    }

    let isCtrlPressed = false;
    let isSPressed = false;

    function handleKeyDown(event: KeyboardEvent) {
        if ($viewingScript === null) {
            return
        }

        if (event.key === 'Control') {
            isCtrlPressed = true;
        } else if (event.key === 's' || event.key === 'S') {
            isSPressed = true;
        }

        if (isCtrlPressed && isSPressed) {
            event.preventDefault();
            saveFile();
        }
    }

    function handleKeyUp(event: KeyboardEvent) {
        if ($viewingScript === null) {
            return
        }

        if (event.key === 'Control') {
            isCtrlPressed = false;
        } else if (event.key === 's' || event.key === 'S') {
            isSPressed = false;
        }
    }

    
</script>

<svelte:window
        on:keydown={handleKeyDown}
        on:keyup={handleKeyUp}
/>

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
                    <span class="label label-text"
                        >Enter "confirm" to delete the the script "{$viewingScript.fileName}":</span
                    >
                    <input
                        type="text"
                        id={$viewingScript.fileName}
                        bind:value={deleteFileConfirm}
                        class="input input-bordered w-full max-w-xs"
                    />
                    <input class="hidden" type="submit" />
                </div>
            </form>

            <form method="dialog" class="modal-backdrop">x
                <button>close</button>
            </form>
        </dialog>

        <div class="justify-center w-full join">
            <button
                on:click={saveFile}
                data-tip="save script"
                class="tooltip w-[70%] btn bg-green-600 hover:bg-green-800 text-gray-100 join-item"
            >
                Save
            </button>
            <button
                on:click={syncScript}
                data-tip="sync script"
                class="tooltip w-[15%] btn bg-blue-500 hover:bg-blue-700 text-gray-100 join-item"
            >
                <!-- TODO: icon :) -->
                🔄
            </button>

            <button
                onclick="confirmDeleteModal.showModal()"
                data-tip="delete script"
                class="tooltip w-[15%] btn bg-red-500 hover:bg-red-700 text-gray-100 join-item"
            >
                <!-- TODO: icon :) -->
                🗑️
            </button>
        </div>
    {:else}
        <div class="justify-center w-full join">
            <button
                class="w-[100%] btn bg-[#1ed760] text-black hover:bg-[#1ed760] hover:scale-[102%] join-item"
                onclick="fileCreateModal.showModal()"
                >Create a script
            </button>
        </div>

        <FileCreateModal />
    {/if}
</section>
