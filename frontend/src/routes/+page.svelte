<script lang="ts">
    import Editor from "../components/Editor.svelte";
    import Sidebar from "../components/Sidebar.svelte";
    import { viewingScript, visitedTabs } from "../stores";
    import { ScriptService } from "$lib/util/api/script/ScriptService";

    async function navigateToScript(innerText: string) {
        const script = await ScriptService.findByName(innerText)
        viewingScript.set(script)
    }
</script>

<main>
    <div class="flex flex-row bg-[#000000]">
        <div class="basis-[20.01%]">
            <Sidebar/>
        </div>

        <div class="grow">
            <!-- TODO: tab logic overflow -->
             <!--{#if $viewingScript !== null}
                <div class="tabs bg-black">
                    {#each $visitedTabs as tab}
                        {#if $viewingScript?.fileName === tab}
                            <a class="tab-md tab-lifted tab-active">{tab}</a>
                        {:else}
                            <a class="tab-md tab-lifted"
                               on:click={(event) => navigateToScript(event.target.innerText)}>{tab}</a>
                        {/if}
                    {/each}
                </div>
            {/if}-->
            <Editor/>
        </div>
    </div>
</main>
