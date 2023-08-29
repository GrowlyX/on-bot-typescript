import { get } from "svelte/store"
import { viewingScript, visitedTabs } from "../../../stores"
import { copyAndRemoveValue } from "../copyArr"
import { refreshFileList } from "./refreshFileList"
import { ToastManager } from "../toast/ToastManager"
import { ScriptService } from "$lib/util/api/script/ScriptService";

export const deleteFile = async () => {
    await ScriptService.deleteByName(get(viewingScript)?.fileName!!)
    await refreshFileList()

    visitedTabs.set(copyAndRemoveValue(get(visitedTabs), get(viewingScript)?.fileName!!))

    // reset the viewing script to dispose of current model
    viewingScript.set(null)
    ToastManager.dispatch("Script was deleted.", "failure")
}
