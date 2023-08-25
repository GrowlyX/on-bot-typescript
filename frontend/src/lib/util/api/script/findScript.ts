import type {Script} from "$lib/models/models";
import {fetchData} from "$lib/util/api/fetchData";

export async function findScript(id: number): Promise<Script> {
    return fetchData(`/api/scripts/find/${id}`)
}
