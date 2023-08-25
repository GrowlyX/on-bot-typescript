import type {Script} from "$lib/models/models";
import {fetchData} from "$lib/util/fetchData";

export async function findAllScripts(): Promise<Script[]> {
    return fetchData("/api/scripts/list")
}
