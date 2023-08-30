import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
    plugins: [sveltekit()],
    ssr: {
        noExternal: ["svelte-hero-icons"]
    },
    server: {
        proxy: {
            '/api': 'http://localhost:6969',
        },
    },
});
