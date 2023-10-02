import type { CreateScript, CreateScriptResponse, ScriptGenericResponse, Script } from "$lib/models/models"
import { deleteData, fetchData, postData } from "$lib/util/api/fetchData";

class ScriptService {

    public static async create(creation: CreateScript): Promise<CreateScriptResponse> {
        return postData(`/api/scripts/create`, creation)
    }

    public static async deleteByName(name: string): Promise<ScriptGenericResponse> {
        return postData(`/api/scripts/delete-name/`, { name: name })
    }

    public static async findAll(): Promise<Script[]> {
        return fetchData("/api/scripts/list")
    }

    public static async findByName(name: string): Promise<Script> {
        return postData(`/api/scripts/find-name/`, { name: name })
    }

    public static async updateContent(script: Script): Promise<ScriptGenericResponse> {
        return postData(`/api/scripts/update-content`, script)
    }

}

export { ScriptService }



