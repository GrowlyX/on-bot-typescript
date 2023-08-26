import type {ScriptGenericResponse} from "$lib/models/models";
import {deleteData, postData} from "$lib/util/api/fetchData";

export async function deleteScript(id: number): Promise<ScriptGenericResponse> {
    return deleteData(`/api/scripts/delete/${id}`)
}

export async function deleteScriptByName(name: string): Promise<ScriptGenericResponse> {
    return postData(`/api/scripts/delete-name`, {
        name: name
    })
}
