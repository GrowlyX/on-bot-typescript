<script lang="ts">
    import type { ToastType } from "$lib/util/toast/ToastEvent"
    import { toasts } from "../stores"
    import { Notification } from "@svelteuidev/core";

    const color = (type: ToastType) => ({
        success: "green",
        failure: "red",
        warning: "yellow",
        info: "indigo"
    }[type])
</script>

<aside>
    <div class="toast toast-start">
        {#each $toasts as toast}
            <Notification
                    title="{toast.title}"
                    color="{color(toast.type)}"
                    withCloseButton={false}
            >
                {toast.description}
            </Notification>
        {/each}
    </div>
</aside>
