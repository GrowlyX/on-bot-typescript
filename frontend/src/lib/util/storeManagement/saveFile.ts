import { get } from "svelte/store"
import { viewingScript } from "../../../stores"
import { updateScriptContent } from "../api/script/updateScriptContent"
import { ToastManager } from "../toast/ToastManager"

export const saveFile = async () => {
    await updateScriptContent(get(viewingScript)!!)
    ToastManager.dispatch("Script was saved.", "success")
}