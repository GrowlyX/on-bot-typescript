export const uniqBy = <T>(arr: T[], predicate: string | ((e: T) => T)) => {
    const cb = typeof predicate === "function"
        ? predicate
        : (o: { [x: string]: any; }) => o[predicate];

    return [...arr.reduce((map, item) => {
        const key = (item === null || item === undefined) ?
            item : cb(item);

        map.has(key) || map.set(key, item);

        return map;
    }, new Map()).values()];
};