export function accumulateBackwards<T>(
    arr: T[],
    accumulator: (parent: T, child: T) => T
): T {
    const result: T[] = [...arr];
    const length = result.length;

    for (let i = length - 1; i > 0; i--) {
        const parent = result[i - 1]
        const child = result[i]

        result[i - 1] = accumulator(parent, child)
    }

    return result[0];
}
