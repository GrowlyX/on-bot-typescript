import { files, getScriptNames } from "../../../stores"

export async function refreshFileList() {
    files.set(await getScriptNames())
}