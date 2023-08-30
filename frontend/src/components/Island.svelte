<script lang="ts">
    import { viewingScript } from "../stores"

    import { syncScript } from "$lib/util/storeManagement/syncScript"
    import { saveFile } from "$lib/util/storeManagement/saveFile"

    import FileCreateModal from "./FileManagement/FileCreateModal.svelte"
    import { CloudArrowDown, Icon, Trash } from "svelte-hero-icons";
    import FileDeleteModal from "./FileManagement/FileDeleteModal.svelte";
    import { deleteFile } from "$lib/util/storeManagement/deleteFile";

    let modalOpen = false
</script>

<section class="flex justify-center p-5">
    {#if $viewingScript !== null}
        <div class="justify-center w-full join">
            <button
                    on:click={saveFile}
                    data-tip="save script"
                    class="tooltip w-[70%] bg-[#1ed760] btn text-black hover:bg-[#1ed760] hover:scale-[102%] join-item"
            >
                Save
            </button>
            <button
                    on:click={$syncScript}
                    data-tip="sync script"
                    class="tooltip w-[15%] btn bg-blue-500 hover:bg-blue-700 text-gray-100 join-item"
            >
                <Icon src="{CloudArrowDown}" solid/>
            </button>

            <button
                    on:click={() => modalOpen = true}
                    data-tip="delete script"
                    class="tooltip w-[15%] btn bg-red-500 hover:bg-red-700 text-gray-100 join-item"
            >
                <Icon src="{Trash}" solid/>
            </button>
        </div>

        <FileDeleteModal
                opened={modalOpen}
                onClose={() => {
                    modalOpen = false
                }}
                onDelete={() => {
                    deleteFile()
                }}
        />
    {:else}
        <div class="justify-center w-full join">
            <button
                    class="w-[100%] btn bg-[#1ed760] text-black hover:bg-[#1ed760] hover:scale-[102%]"
                    on:click={() => modalOpen = true}
            >Create a script
            </button>
        </div>

        <FileCreateModal
                opened={modalOpen}
                onClose={() => {
                    modalOpen = false
                }}
        />
    {/if}
</section>
