import { writable } from 'svelte/store';

export const syncScript = writable<() => void>(() => { })
