<script lang="ts">
    import FileTree from "./FileDisplay/FileTree.svelte";
    import Runner from "./Runner.svelte";
    import type {Script} from "$lib/models/models";
    import { onMount } from "svelte";
    import {findAllScripts} from "$lib/util/findAllScripts";

    // TODO: handle no files :)
    let files: string[] = []

    onMount(async () => {
        findAllScripts()
            .then((values: Script[]) => {
                files = values
                    .map((script) => script.fileName)
            })
    })
</script>

<aside>
    <FileTree {files}/>

    <Runner/>
</aside>
