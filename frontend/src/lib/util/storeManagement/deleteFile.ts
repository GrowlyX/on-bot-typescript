import { get } from "svelte/store"
import { viewingScript, visitedTabs } from "../../../stores"
import { copyAndRemoveValue } from "../copyArr"
import { refreshFileList } from "./refreshFileList"
import { ToastManager } from "../toast/ToastManager"
import { ScriptService } from "$lib/util/api/script/ScriptService";

export const deleteFile = async () => {
    const fileName = get(viewingScript)?.fileName!!
    await ScriptService.deleteByName(fileName)
    await refreshFileList()

    visitedTabs.set(copyAndRemoveValue(get(visitedTabs), fileName))

    // reset the viewing script to dispose of current model
    viewingScript.set(null)
    ToastManager.dispatch("Script Deleted", `Deleted script by name ${fileName}.`, "failure")
}
