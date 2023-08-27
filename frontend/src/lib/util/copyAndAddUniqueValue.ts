export function copyAndAddUniqueValue<T>(arr: T[], newValue: T): T[] {
    if (arr.includes(newValue)) {
        return arr.slice();
    } else {
        return [...arr, newValue];
    }
}
