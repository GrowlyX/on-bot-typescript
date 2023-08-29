<script>
    import "../app.css";
    import MetaTitle from "../components/Meta/MetaTitle.svelte";
    import Toast from "../components/Toast.svelte"
</script>

<slot />
<Toast/>
<MetaTitle _title="Home" />
