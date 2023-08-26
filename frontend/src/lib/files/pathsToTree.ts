import { compose } from "$lib/util/compose";
import { uniqBy } from "$lib/util/uniqBy";
import type { TFile } from "./TFile";

export const merge = (trees: TFile[]): TFile[] => {
    const result = [] as TFile[]

    for (const tree of trees) {
        const existing = trees.filter(item => item.name === tree.name)
        const files = existing.map(it => it.files).flat()

        result.push({ name: tree.name, path: "__node__", files })
    }

    // TODO: actually spend time in this function so that the logic isn't shit
    // TODO: recursively merge because im like 99% sure that this doesn't work past depth = 1 for the tree
    return uniqBy(result, "name")
}

export const pathToTree = (path: string): TFile => {
    const lineage = (path: string) => path.split("/");
    const asFiles = (lineage: string[]) => lineage.map(name => ({ name, path, files: [] }))
    const nest = (files: TFile[]) => files.reduce((parent, child) => ({ name: parent.name, path, files: [child] }))

    return compose(nest, asFiles, lineage)(path);
}

export const pathsToTree = (paths: string[]): TFile[] => {
    const trees = paths.map(pathToTree)

    return merge(trees);
}
