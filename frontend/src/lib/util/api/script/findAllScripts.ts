import type { Script } from "$lib/models/models";
import { fetchData } from "$lib/util/api/fetchData";

export async function findAllScripts(): Promise<Script[]> {
    return fetchData("/api/scripts/list")
}
