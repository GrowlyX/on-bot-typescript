<script lang="ts">
    import FileTree from "./FileDisplay/FileTree.svelte";
    import Runner from "./Runner.svelte";
    import type {Script} from "$lib/models/models";
    import { onMount } from "svelte";
    import {findAllScripts} from "$lib/util/api/script/findAllScripts";
    import {fetchAPIStatus} from "$lib/util/api/fetchAPIStatus";
    import {writable} from "svelte/store";

    // TODO: handle no files :)
    const files = writable<string[]>([])

    onMount(async () => {
        const scripts = await findAllScripts()
        files.set(
            scripts
                .map((script: Script) =>
                    script.fileName
                )
        )

        // TODO: api status sidebar thing
        console.log(
            await fetchAPIStatus()
        )
    })
</script>

<aside>
    <FileTree files={$files}/>

    <Runner/>
</aside>
