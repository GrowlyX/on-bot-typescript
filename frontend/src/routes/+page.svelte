<script lang="ts">
    import Editor from "../components/Editor.svelte";
    import Sidebar from "../components/Sidebar.svelte";
    import { viewingScript, visitedTabs } from "../stores.ts";
    import { findScriptByName } from "$lib/util/api/script/findScript.ts";

    async function navigateToScript(innerText: string) {
        const script = await findScriptByName(innerText)
        viewingScript.set(script)
    }
</script>

<main>
    <div class="flex flex-row bg-zinc-800">
        <div class="basis-[18%]">
            <Sidebar/>
        </div>

        <div class="grow">
            <!-- tab logic !-->
            {#if $viewingScript !== null}
                <div class="tabs">
                    {#each $visitedTabs as tab}
                        {#if $viewingScript?.fileName === tab}
                            <a class="tab tab-lifted tab-active">{tab}</a>
                        {:else}
                            <a class="tab tab-lifted"
                               on:click={(event) => navigateToScript(event.target.innerText)}>{tab}</a>
                        {/if}
                    {/each}
                </div>
            {:else}
                <div class="text-center m-20 text-blue-50">
                    <h1 class="font-bold text-2xl">Not editing</h1>
                    <p class="font-thin">Click a script on the sidebar to start editing!</p>
                </div>
            {/if}

            <Editor/>
        </div>
    </div>
</main>

<style>
</style>
