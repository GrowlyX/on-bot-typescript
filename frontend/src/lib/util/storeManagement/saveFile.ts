import { get } from "svelte/store"
import { viewingScript } from "../../../stores"
import { updateScriptContent } from "../api/script/updateScriptContent"
import { dispatchToast } from "../toast/dispatchToast"

export const saveFile = async () => {
    await updateScriptContent(get(viewingScript)!!)
    dispatchToast("Script was saved.", "success")
}