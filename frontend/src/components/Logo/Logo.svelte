<script lang="ts">
import {viewingScript} from "../../stores.ts";
</script>

<!-- svelte-ignore a11y-no-static-element-interactions -->
<!-- svelte-ignore a11y-click-events-have-key-events -->
<div on:click={() => { viewingScript.set(null) }} class="flex flex-col justify-center hover:bg">
    <img class="cursor-pointer hover:bg-[#2a2a2a] rounded-md scale-75"
         src="logo.png" id="logo" alt="OnBot Kotlin Logo">
</div>
