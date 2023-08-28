import { viewingScript } from "../../../stores"
import { findScriptByName } from "../api/script/findScript"
import { dispatchToast } from "../toast/dispatchToast"
import { get } from 'svelte/store';

export const syncScript = async () => {
    const script = await findScriptByName(get(viewingScript)!!.fileName)
    viewingScript.set(script)

    dispatchToast("Script synced.", "info")
}