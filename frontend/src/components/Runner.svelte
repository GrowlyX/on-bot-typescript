<script lang="ts">
    import { files, getScriptNames, viewingScript, visitedTabs } from "../stores";
    import { updateScriptContent } from "$lib/util/api/script/updateScriptContent";
    import { deleteScriptByName } from "$lib/util/api/script/deleteScript";
    import { createScript } from "$lib/util/api/script/createScript";
    import { copyAndRemoveValue } from "$lib/util/copyArr";
    import { findScriptByName } from "$lib/util/api/script/findScript";

    let fileName = "example.kts"
    let deleteFileConfirm = ""

    async function refreshFileList() {
        files.set(await getScriptNames());
    }

    async function syncScript() {
        const script = await findScriptByName(path)
        viewingScript.set(script)
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
        console.log("deleted");
    }

    async function saveFile() {
        const resp = await updateScriptContent($viewingScript!!)
        console.log("Updated script content: " + resp);
    }

    async function createFile() {
        if (fileName.includes(' ') || !fileName.endsWith(".kts")) {
            // TODO: better input validation reporting
            console.log('invalid name')
            return
        }

        try {
            await createScript({fileName})
            await refreshFileList();
        } catch (error: any) {
            console.log('did not work: ' + error.toString())
        }
    }

</script>

<section class="flex justify-center p-5">
    {#if $viewingScript !== null}
        <dialog id="confirmDeleteModal" class="modal">
            <form on:submit={deleteFile} method="dialog" class="modal-box">
                <div class="form-control w-full max-w-xs">
                    <span class="label label-text">Enter "confirm" to delete the the script "{$viewingScript.fileName}":</span>
                    <input type="text" id="{$viewingScript.fileName}" bind:value={deleteFileConfirm} class="input input-bordered w-full max-w-xs"/>
                    <input class="hidden" type="submit"/>
                </div>
            </form>

            <form method="dialog" class="modal-backdrop">
                <button>close</button>
            </form>
        </dialog>

        <div class="justify-center w-full join">
            <button on:click={saveFile} data-tip="save script" class="tooltip w-[60%] btn bg-green-600 hover:bg-green-800  text-gray-100 join-item">Save</button>
            <button on:click={syncScript} data-tip="sync script" class="tooltip w-[20%] btn bg-blue-500 hover:bg-blue-700 text-gray-100 join-item">🔄</button>

            <button onclick="confirmDeleteModal.showModal()" data-tip="delete script" class="tooltip w-[20%] btn bg-red-500 hover:bg-red-700 text-gray-100 join-item">❌</button>
        </div>
    {:else}
        <div class="justify-center w-full join">
            <button data-tip="create a script" class="tooltip w-[100%] btn bg-green-600 text-gray-100 hover:bg-green-800 join-item" onclick="fileCreateModal.showModal()">Create</button>
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
