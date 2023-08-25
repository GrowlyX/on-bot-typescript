import type {Script, ScriptGenericResponse} from "$lib/models/models";
import {putData} from "$lib/util/fetchData";

export async function updateScriptContent(script: Script): Promise<ScriptGenericResponse> {
    return putData(`/api/scripts/update-content/`, script)
}
