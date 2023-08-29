import { writable } from "svelte/store";
import type { APIStatus, Script } from "$lib/models/models";
import { findAllScripts } from "$lib/util/api/script/findAllScripts";
import { onMount } from "svelte";
import { fetchAPIStatus } from "$lib/util/api/fetchAPIStatus";

import { _api } from '@iconify/svelte';
import fetch from 'cross-fetch';
import type { ToastEvent } from "$lib/util/toast/ToastEvent";

_api.setFetch(fetch);

export const toasts = writable<ToastEvent[]>([])

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
export const title = writable<string>("Home")

export const visitedTabs = writable<string[]>([])

export const apiStatus = writable<APIStatus | null>(null, () => {
    let interval = setInterval(async () => {
        fetchAPIStatus()
            .then((status: APIStatus) => {
                apiStatus.set(status)
            })
            .catch(() => {
                apiStatus.set(null)
            })
    }, 500)

    return () => {
        clearInterval(interval)
    }
})
