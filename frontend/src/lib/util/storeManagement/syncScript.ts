import { viewingScript } from "../../../stores"
import { findScriptByName } from "../api/script/findScript"
import { get } from 'svelte/store';
import { ToastManager } from "../toast/ToastManager";

export const syncScript = async () => {
    const script = await findScriptByName(get(viewingScript)!!.fileName)
    viewingScript.set(script)

    ToastManager.dispatch("Script synced.", "info")
}