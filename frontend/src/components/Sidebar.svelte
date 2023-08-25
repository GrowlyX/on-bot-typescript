<script lang="ts">
    import FileTree from "./FileDisplay/FileTree.svelte";
    import Runner from "./Runner.svelte";
    import type {Script} from "$lib/models/models";
    import { onMount } from "svelte";

    // TODO: handle no files :)
    let files: string[] = []

    onMount(async () => {
        fetch("/api/scripts/list")
            .then(response => {
                if (!response.ok) {
                    throw new Error(`response was not ok: ${response.status}`);
                }
                return response.json();
            })
            .then(values => {
                files = values
                    .map((script: Script) => script.fileName)
            })
    })
</script>

<aside>
    <FileTree {files}/>

    <Runner/>
</aside>
