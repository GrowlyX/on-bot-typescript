<script>
    import "../app.css";
    import FileDeleteModal from "../components/FileManagement/FileDeleteModal.svelte"
    import MetaTitle from "../components/Meta/MetaTitle.svelte";
    import Toast from "../components/Toast.svelte"
</script>

<slot />
<Toast/>
<MetaTitle _title="Home" />
<FileDeleteModal/>
