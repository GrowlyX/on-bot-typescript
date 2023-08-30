import { get } from "svelte/store"
import { viewingScript } from "../../../stores"
import { ToastManager } from "../toast/ToastManager"
import { ScriptService } from "$lib/util/api/script/ScriptService";

export const saveFile = async () => {
    await ScriptService.updateContent(get(viewingScript)!!)
    ToastManager.dispatch("Saved Script", "Saved the script.", "success")
}
