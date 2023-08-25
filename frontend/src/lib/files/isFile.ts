// ! will not work unless things have been split by slashes !
export const isFile = (name: string) => {
    return /(\w)+\.(\w)+/g.test(name);
}