import { compose } from "$lib/util/compose";
import { uniqBy } from "$lib/util/uniqBy";
import type { TFile } from "./TFile";
import { accumulateBackwards } from "$lib/util/accumulateBackwards";

export const merge = (trees: TFile[]): TFile[] => {
    const result = [] as TFile[]

    for (const tree of trees) {
        const existing = trees.filter(item => item.name === tree.name)
        const files = existing.map(it => it.files).flat()

        result.push({
            name: tree.name,
            // we assume that all directories contain at least one file due to the behavior of directories
            path: files.length == 0 ? tree.name : "__node__",
            files: merge(files)
        })
    }

    return uniqBy(result, "name")
}

export const pathToTree = (path: string): TFile => {
    const lineage = (path: string) => path.split("/");
    const asFiles = (lineage: string[]) => lineage.map(name => ({name, path, files: []}))
    const nest = (files: TFile[]) => accumulateBackwards<TFile>(files, ((parent, child) => ({name: parent.name, path, files: [child]})))

    return compose(nest, asFiles, lineage)(path);
}

export const pathsToTree = (paths: string[]): TFile[] => {
    const trees = paths.map(pathToTree)

    return merge(trees);
}
