<script lang="ts">
    import type { ToastType } from "$lib/util/toast/ToastEvent"
    import { toasts } from "../stores"

    // TODO: style well
    const style = (type: ToastType) => ({
        success: "success",
        failure: "error",
        warning: "warning",
        info: "info"
    }[type])
</script>

<aside>
    <div class="toast toast-start">
        {#each $toasts as toast}
            <div class="alert alert-{style(toast.type)}">
                <span>{toast.text}</span>
            </div>
        {/each}
    </div>
</aside>
