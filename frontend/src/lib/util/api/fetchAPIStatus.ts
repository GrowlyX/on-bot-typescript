import {fetchData} from "$lib/util/api/fetchData";
import type {APIStatus} from "$lib/models/models";

export async function fetchAPIStatus(): Promise<APIStatus> {
    return fetchData("/api/status")
}
