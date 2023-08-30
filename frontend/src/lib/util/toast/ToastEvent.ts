export type ToastType = "success" | "failure" | "warning" | "info"
export type ToastEvent = { title: string, description: string, type: ToastType, duration: number, id: string }
