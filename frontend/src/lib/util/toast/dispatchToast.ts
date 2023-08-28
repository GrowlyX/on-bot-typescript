import { createEventDispatcher } from "svelte"

export const dispatchToast = (text: string, type: "success" | "failure" | "warning" | "info") => {
    const dispatch = createEventDispatcher()

    dispatch("toast", { text, type })
}
