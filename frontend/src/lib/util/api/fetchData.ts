export async function postData<T, U>(path: string, data: T): Promise<U> {
    return fetchDataWithBody(path, 'POST', data)
}

export async function deleteData<U>(path: string): Promise<U> {
    return fetchDataWithMethod(path, 'DELETE')
}

export async function fetchData<T>(path: string): Promise<T> {
    const response = await fetch(path);

    if (!response.ok) {
        throw new Error(`response was not ok: ${response.status}`);
    }

    return await response.json() as T;
}

async function fetchDataWithMethod<U>(path: string, method: string): Promise<U> {
    const response = await fetch(path, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        throw new Error(`response was not ok: ${response.status}`);
    }

    return await response.json() as U;
}

async function fetchDataWithBody<T, U>(path: string, method: string, data: T): Promise<U> {
    const response = await fetch(path, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        Promise.reject({ status: response.status, details: await response.text() });
    }

    return await response.json() as U;
}
