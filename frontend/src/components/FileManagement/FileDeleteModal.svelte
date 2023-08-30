<script lang="ts">
    import { Modal, Button } from '@svelteuidev/core';

    export let opened: boolean
    export let onClose: () => void;
    export let onDelete: () => void;
</script>

<Modal
        {opened}
        centered
        title="Do you want to delete this script?"
        on:close={onClose}
>
    <div class="flex flex-row gap-2">
        <Button
                color="red"
                on:click={() => {
                    onDelete()
                    onClose()
                }}
        >
            Delete
        </Button>

        <Button
                color="gray"
                on:click={onClose}
        >
            Cancel
        </Button>
    </div>
</Modal>

