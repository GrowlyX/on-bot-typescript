import { get } from "svelte/store"
import { viewingScript, visitedTabs } from "../../../stores"
import { deleteScriptByName } from "../api/script/deleteScript"
import { copyAndRemoveValue } from "../copyArr"
import { refreshFileList } from "./refreshFileList"
import { ToastManager } from "../toast/ToastManager"

export const deleteFile = async () => {
    await deleteScriptByName(get(viewingScript)?.fileName!!)
    await refreshFileList()

    visitedTabs.set(copyAndRemoveValue(get(visitedTabs), get(viewingScript)?.fileName!!))

    // reset the viewing script to dispose of current model
    viewingScript.set(null)
    ToastManager.dispatch("Script was deleted.", "failure")
}