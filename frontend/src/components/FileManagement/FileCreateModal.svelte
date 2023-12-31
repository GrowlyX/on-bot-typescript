<script lang="ts">
    import { ToastManager } from "$lib/util/toast/ToastManager"
    import { ScriptService } from "$lib/util/api/script/ScriptService";
    import { refreshFileList } from "$lib/util/storeManagement/refreshFileList";
    import { Input, Modal } from "@svelteuidev/core"

    export let opened: boolean
    export let onClose: () => void;
    let name = ""

    type Failure = {
        error: string
        enabled: boolean
    }

    let failure: Failure = {
        error: "",
        enabled: false
    }

    function onKeyPress(event: KeyboardEvent) {
        validateScriptName()

        if (event.key === 'Enter') {
            if (failure.enabled) {
                return
            }

            createFile()
        }
    }

    async function validateScriptName() {
        if (name.length === 0) {
            failure = {
                error: "Script name cannot be empty!",
                enabled: true
            }
            return
        }

        if (/\s+/.test(name)) {
            failure = {
                error: "Script name must not contain whitespaces!",
                enabled: true
            }
            return
        }

        try {
            await ScriptService.findByName(`${name}.ts`)

            failure = {
                error: "Script already exists!",
                enabled: true
            }
            return
        } catch (ignored) {

        }

        failure = {
            error: "",
            enabled: false
        }
    }

    const createFile = async () => {
        if (failure.enabled) {
            return
        }

        try {
            await ScriptService.create({fileName: `${name}.ts`})
            await refreshFileList()

            onClose()
            ToastManager.dispatch("New Script", `Created a new script with the name ${name}.ts.`, "success")
        } catch (error: any) {
            ToastManager.dispatch("Failure", error.error, "failure")
        }
    }
</script>

<Modal
        {opened}
        centered
        title="Enter a file name"
        on:close={() => {
            opened = false
            onClose()
        }}
>
    <Input
            on:keyup={validateScriptName}
            on:keypress={onKeyPress}
            invalid={failure.enabled}
            placeholder="Shared"
            bind:value={name}
    />

    {#if failure.enabled}
        <span class="label label-text text-red-500">{failure.error}</span>
    {/if}
</Modal>
