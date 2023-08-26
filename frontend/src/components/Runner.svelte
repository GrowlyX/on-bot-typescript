<script lang="ts">
    import {files, getScriptNames, viewingScript} from "../stores";
    import {updateScriptContent} from "$lib/util/api/script/updateScriptContent";
    import {getNotificationsContext} from 'svelte-notifications';
    import {deleteScriptByName} from "$lib/util/api/script/deleteScript";
    import {createScript} from "$lib/util/api/script/createScript";

    async function handleFileNameSubmitted(event: CustomEvent<string>) {
        let fileName = event.detail;

        if (!fileName.endsWith(".kts")) {
            console.log("invalid file name!")
            return
        }

        await createScript({
            fileName: fileName
        })

        files.set(await getScriptNames())
    }

    async function deleteFile() {
        await deleteScriptByName($viewingScript?.fileName)
        files.set(await getScriptNames())

        // reset the viewing script to dispose of current model
        viewingScript.set(null)
        console.log('deleted')
    }

    async function saveFile() {
        const resp = await updateScriptContent($viewingScript)
        console.log("Updated script content: " + resp)

        const {addNotification} = getNotificationsContext();

        // TODO: lol doesn't work
        addNotification({
            text: "Saved file!",
            position: 'bottom-left',
        })
    }
</script>

<section class="flex justify-center p-5 m-5 rounded-md bg-zinc-700">
    <!--<FileNamePrompt
            on:fileNameSubmitted={handleFileNameSubmitted}
            bind:isOpen={isPromptOpen}
    />-->

    {#if $viewingScript !== null}
        <div class="justify-center w-full join">
            <button on:click={saveFile} class="w-1/2 btn btn-success join-item">Save</button>
            <button on:click={deleteFile} class="w-1/2 btn btn-danger join-item">Delete</button>
        </div>
    {:else}
        <div class="justify-center w-full join">
            <button class="w-1/2 btn btn-success join-item">Create</button>
        </div>
    {/if}
</section>
