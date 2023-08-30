<script>
    import "../app.css";
    import MetaTitle from "../components/Meta/MetaTitle.svelte";
    import Toast from "../components/Toast.svelte"
    import {SvelteUIProvider} from "@svelteuidev/core";
</script>

<SvelteUIProvider themeObserver="dark">
    <slot />
    <MetaTitle _title="Home" />
    <Toast/>
</SvelteUIProvider>
