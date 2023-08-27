export function copyArr<T>(arr: T[], newValue: T): T[] {
    if (arr.includes(newValue)) {
        return arr.slice()
    } else {
        return [...arr, newValue]
    }
}

export function copyAndRemoveValue<T>(arr: T[], valueToRemove: T): T[] {
    const index = arr.indexOf(valueToRemove)

    if (index !== -1) {
        return arr.filter((_, i) => i !== index)
    } else {
        return arr.slice()
    }
}
