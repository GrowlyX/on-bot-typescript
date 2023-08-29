<script lang="ts">
    import { toasts } from "../stores"

    // TODO: style well
</script>

<aside>
    {#each $toasts as toast}
        <div class="toast toast-start">
            <div class="alert alert-{toast.type}">
                <span>{toast.text}</span>
            </div>
        </div>
    {/each}
</aside>
