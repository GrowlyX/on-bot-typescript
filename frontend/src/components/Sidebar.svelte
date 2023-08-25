<script lang="ts">
    import FileTree from "./FileDisplay/FileTree.svelte";
    import Runner from "./Runner.svelte";
    import type {Script} from "$lib/models/models";
    import { onMount } from "svelte";
    import {findAllScripts} from "$lib/util/findAllScripts";

    // TODO: handle no files :)
    let files: string[] = []

    onMount(async () => {
        const scripts = await findAllScripts()
        files = scripts
            .map((script: Script) =>
                script.fileName
            )
    })
</script>

<aside>
    <FileTree {files}/>

    <Runner/>
</aside>
