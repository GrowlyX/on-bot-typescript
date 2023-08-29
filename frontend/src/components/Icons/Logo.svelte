<script lang="ts">
import {viewingScript} from "../../stores.ts";
</script>

<!-- svelte-ignore a11y-no-static-element-interactions -->
<!-- svelte-ignore a11y-click-events-have-key-events -->
<div on:click={() => { viewingScript.set(null) }} class="flex flex-col justify-center hover:bg-[#2a2a2a] transition-colors rounded-md m-4 cursor-pointer">
    <img class="logo scale-75"
         src="logo.png" id="logo" alt="OnBot Kotlin Logo">
</div>

<style>
    #logo {
        width: 30rem;
    }
</style>
