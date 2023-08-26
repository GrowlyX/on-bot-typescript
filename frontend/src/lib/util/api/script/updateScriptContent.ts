import type {Script, ScriptGenericResponse} from "$lib/models/models";
import {postData} from "$lib/util/api/fetchData";

export async function updateScriptContent(script: Script): Promise<ScriptGenericResponse> {
    return postData(`/api/scripts/update-content`, script)
}
