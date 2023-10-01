export type Script = {
    fileName: string,
    fileContent: string
}

export type CreateScript = {
    fileName: string
}

export type CreateScriptResponse = {
    creationDate: number
}

export type ScriptGenericResponse = {
    lastEdited: string
}

export type APIStatus = {
    hotReloadEnabled: boolean
    scriptEngineBooted: boolean
}
