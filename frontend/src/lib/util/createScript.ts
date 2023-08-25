import type {CreateScript, CreateScriptResponse} from "$lib/models/models";
import {postData} from "$lib/util/fetchData";

export async function createScript(creation: CreateScript): Promise<CreateScriptResponse> {
    return postData(`/api/scripts/create/`, creation)
}
