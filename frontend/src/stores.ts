import { writable } from "svelte/store";
import type { Script } from "$lib/models/models";
import { findAllScripts } from "$lib/util/api/script/findAllScripts";
import { onMount } from "svelte";

export async function getScriptNames(): Promise<string[]> {
    const backingScripts = await findAllScripts()
    return backingScripts
        .map((script: Script) =>
            script.fileName
        )
}

export const files = writable(
    [] as string[],
    function start(set) {
        onMount(async () => {
            set(await getScriptNames())
        })
    }
);

export const viewingScript = writable<Script | null>(null)
export let title = writable<string>("Home")
