<script>
    import "../app.css";
    import MetaTitle from "../components/Meta/MetaTitle.svelte";
    import Toast from "../components/Toast.svelte"
    import FileDeleteModal from "../components/FileManagement/FileDeleteModal.svelte";
</script>

<slot />
<Toast/>
<MetaTitle _title="Home" />
<FileDeleteModal/>
