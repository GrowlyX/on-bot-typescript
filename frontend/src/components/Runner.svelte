<script lang="ts">
    import {viewingScript} from "../stores";
    import {updateScriptContent} from "$lib/util/api/script/updateScriptContent";
    import { getNotificationsContext } from 'svelte-notifications';

    const { addNotification } = getNotificationsContext();

    async function saveFile() {
        // TODO: gray it out?
        if ($viewingScript == null) {
            return
        }

        const resp = await updateScriptContent($viewingScript)
        console.log("Updated script content: " + resp)

        // TODO: lol doesn't work
        addNotification({
            text: "Saved file!",
            position: 'bottom-left',
        })
    }
</script>

<section class="flex justify-center p-5 m-5 rounded-md bg-zinc-700">
    <div class="justify-center w-full join">
        <button class="w-1/2 btn btn-success join-item">Create</button>
        <button on:click={saveFile} class="w-1/2 btn join-item">Save</button>
    </div>
</section>
