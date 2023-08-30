<script lang="ts">
    import { deleteFile } from "$lib/util/storeManagement/deleteFile"
    import { viewingScript } from "../../stores"

    import { Button, Modal } from "flowbite-svelte"
    import { Icon } from "flowbite-svelte-icons"
    let popupModal = false
</script>



<Button on:click={() => (popupModal = true)}>Pop-up modal</Button>

<Modal bind:open={popupModal} size="xs" autoclose class="dark">
    <div class="text-center">
        <Icon
            name="exclamation-circle-outline"
            class="mx-auto mb-4 text-gray-400 w-12 h-12 dark:text-gray-200"
        />
        <h3 class="mb-5 text-lg font-normal text-gray-500 dark:text-gray-400">
            Are you sure you want to delete this product?
        </h3>
        <Button color="red" class="mr-2">Yes, I'm sure</Button>
        <Button color="alternative">No, cancel</Button>
    </div>
</Modal>
