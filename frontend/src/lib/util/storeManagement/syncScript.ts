import { viewingScript } from "../../../stores"
import { get } from 'svelte/store';
import { ToastManager } from "../toast/ToastManager";
import { ScriptService } from "$lib/util/api/script/ScriptService";

export const syncScript = async () => {
    const script = await ScriptService.findByName(get(viewingScript)!!.fileName)
    viewingScript.set(script)

    ToastManager.dispatch("Script synced.", "info")
}
