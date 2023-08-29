import { nanoid } from "nanoid"
import { get } from "svelte/store"
import { toasts } from "../../../stores"
import type { ToastType } from "./ToastEvent"

class ToastManager {
    public static dispatch(text: string, type: ToastType, duration: number = 3000) {
        const id = nanoid()
        const toast = { text, type, duration, id }

        toasts.set([...get(toasts), toast])

        setTimeout(() => this.dismiss(id), toast.duration);
    }

    public static dismiss(id: string) {
        toasts.update(all => all.filter(toast => toast.id !== id))
    }
}

export { ToastManager }
