import type {Script} from "$lib/models/models";
import {fetchData, postData} from "$lib/util/api/fetchData";

export async function findScript(id: number): Promise<Script> {
    return fetchData(`/api/scripts/find/${id}`)
}

export async function findScriptByName(name: string): Promise<Script> {
    return postData(`/api/scripts/find-name/`, {
        name: name
    })
}
