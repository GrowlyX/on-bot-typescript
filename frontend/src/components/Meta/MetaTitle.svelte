<svelte:head>
    <title>OBT - {_title}</title>
</svelte:head>

<script lang="ts">
    import {title} from "../../stores.js";
    export let _title = "Home"

    title.subscribe((newTitle: string) => {
        _title = newTitle
    })
</script>
