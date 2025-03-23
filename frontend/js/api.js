const API_BASE_URL = "localhost:8080/api/";

async function request(endpoint, options={}) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer `,
                ...options.headers,
            },
            ...options
        });

        if (!response.ok) {
            throw new Error(`API 요청 에러: ${response.status}: ${response.statusText}`)
        }
        return response.json();
    } catch(error) {
        console.error(error.message);
        throw error;
    }
}

export function api_login(email, passwd) {
    return request("login", {
        method: "POST",
        body: JSON.stringify({email, passwd}),
    });
}