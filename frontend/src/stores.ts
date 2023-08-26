import {readable, writable} from "svelte/store";
import type {Script} from "$lib/models/models";
import {findAllScripts} from "$lib/util/api/script/findAllScripts";
import {onMount} from "svelte";

export const files = readable(
    [] as string[],
    function start(set) {
        onMount(async () => {
            const backingScripts = await findAllScripts()
            const mapped = backingScripts
                .map((script: Script) =>
                    script.fileName
                )
            set(mapped)
        })
    }
);

export const viewingScript = writable<Script | null>(null)
