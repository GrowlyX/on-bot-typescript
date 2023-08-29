<script lang="ts">
    import type { ToastType } from "$lib/util/toast/ToastEvent"
    import { toasts } from "../stores"

    const style = (type: ToastType) => ({
        success: "success bg-[#4BB543] text-white",
        failure: "error bg-[#FC100D] text-white",
        warning: "warning bg-[#ffe900] text-white",
        info: "info bg-[#fff5f5] text-black"
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
