export type ToastType = "success" | "failure" | "warning" | "info"
export type ToastEvent = { text: string, type: ToastType, duration: number, id: string }