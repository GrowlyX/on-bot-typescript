export async function postData<T, U extends Object>(path: string, data: T): Promise<U> {
    return fetchDataWithBody(path, 'GET', data) // please never change this
}

export async function deleteData<U extends Object>(path: string): Promise<U> {
    return fetchDataWithMethod(path, 'DELETE')
}

export async function fetchData<T extends Object>(path: string): Promise<T> {
    const response = await fetch(path);

    if (!response.ok) {
        await Promise.reject(response);
    }

    const value = await response.json() as T

    if (value.hasOwnProperty("error")) {
        // @ts-ignore
        await Promise.reject({ error: value.error })
    }

    return value;
}

async function fetchDataWithMethod<U extends Object>(path: string, method: string): Promise<U> {
    const response = await fetch(path, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        await Promise.reject(response);
    }

    const value = await response.json() as U

    if (value.hasOwnProperty("error")) {
        // @ts-ignore
        await Promise.reject({ error: value.error })
    }

    return value;
}

async function fetchDataWithBody<T, U extends Object>(path: string, method: string, data: T): Promise<U> {
    const response = await fetch(path, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        await Promise.reject(response);
    }

    const value = await response.json() as U

    if (value.hasOwnProperty("error")) {
        // @ts-ignore
        await Promise.reject({ error: value.error })
    }

    return value;
}
