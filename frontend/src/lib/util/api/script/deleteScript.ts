import type {ScriptGenericResponse} from "$lib/models/models";
import {deleteData} from "$lib/util/api/fetchData";

export async function deleteScript(id: number): Promise<ScriptGenericResponse> {
    return deleteData(`/api/scripts/delete/${id}`)
}
