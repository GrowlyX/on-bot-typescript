<script lang="ts">
    import { refreshFileList } from "$lib/util/storeManagement/refreshFileList"
    import { deleteScriptByName } from "$lib/util/api/script/deleteScript"
    import { copyAndRemoveValue } from "$lib/util/copyArr"
    import { viewingScript, visitedTabs } from "../stores"

    import FileCreateModal from "./FileManagement/FileCreateModal.svelte"
    import { syncScript } from "$lib/util/storeManagement/syncScript"
    import { saveFile } from "$lib/util/storeManagement/saveFile"
    import { deleteFile } from "$lib/util/storeManagement/deleteFile"
    import { onMount } from "svelte"

    let mounted = false

    let confirmDeleteModal: HTMLDialogElement
    let fileCreateModal: HTMLDialogElement

    onMount(() => {
        mounted = true;

        confirmDeleteModal = document.getElementById("confirmDeleteModal") as HTMLDialogElement
        fileCreateModal = document.getElementById("fileCreateModal") as HTMLDialogElement
    })

    viewingScript.subscribe(() => {
        if (mounted) {
            confirmDeleteModal = document.getElementById("confirmDeleteModal") as HTMLDialogElement
            fileCreateModal = document.getElementById("fileCreateModal") as HTMLDialogElement
        }
    })

    let deleteFileConfirm = ""

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
                on:click={() => confirmDeleteModal.showModal()}
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
                on:click={() => fileCreateModal.showModal()}
                >Create a script
            </button>
        </div>

        <FileCreateModal />
    {/if}
</section>
