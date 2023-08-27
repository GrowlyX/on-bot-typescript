<script>
    import Editor from "../components/Editor.svelte";
    import Sidebar from "../components/Sidebar.svelte";
</script>

<main>
    <div class="flex flex-row bg-zinc-800">
        <div class="basis-[18%]">
            <Sidebar />
        </div>

        <div class="grow">
            <Editor />
        </div>
    </div>
</main>

<style>
</style>
