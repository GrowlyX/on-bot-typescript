<script lang="ts">
  import { files, getScriptNames, viewingScript } from "../stores";
  import { updateScriptContent } from "$lib/util/api/script/updateScriptContent";
  import { getNotificationsContext } from "svelte-notifications";
  import { deleteScriptByName } from "$lib/util/api/script/deleteScript";
  import { createScript } from "$lib/util/api/script/createScript";

  let fileName = "example.kts"

  async function refreshFileList() {
    files.set(await getScriptNames());
  }

  async function handleFileNameSubmitted(event: CustomEvent<string>) {
    let fileName = event.detail;

    if (!fileName.endsWith(".kts")) {
      console.log("invalid file name!");
      return;
    }

    await createScript({
      fileName: fileName,
    });

    await refreshFileList();
  }

  async function deleteFile() {
    await deleteScriptByName($viewingScript?.fileName!!);
    await refreshFileList();

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

<section class="flex justify-center p-5 m-5 rounded-md bg-zinc-700">
  {#if $viewingScript !== null}
    <div class="justify-center w-full join">
      <button on:click={saveFile} class="w-1/2 btn btn-success join-item">Save</button>
      <button on:click={deleteFile} class="w-1/2 btn btn-danger join-item">Delete</button>
    </div>
  {:else}
    <div class="justify-center w-full join">
      <button class="w-1/2 btn btn-success join-item" onclick="fileCreateModal.showModal()">Create</button>
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
