const API_BASE_URL = "http://localhost:8080/api";

async function request(endpoint, options={}) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            headers: {
                "Content-Type": "application/json",
                ...options.headers,
            },
            credentials: "include",
            ...options
        });



        if (!response.ok) {
            const data = await response.json();
            if(window.location.pathname !== "/login" && response.status === 403) {
                window.location.href = "./login?logout=expire"
            }
            throw {
                status: response.status,
                message: data.message || '알 수 없는 오류가 발생했습니다.',
                data: data
            };
        }
        return response;
    } catch(error) {
        console.error('API 요청 중 오류:', error);
        throw error;
    }
}

export function api_login(username, password) {
    return request("/login", {
        method: "POST",
        body: JSON.stringify({username, password}),
    });
}

export function api_signup(userData) {
    return request("/user/join", {
        method: "POST",
        body: JSON.stringify(userData),
    });
}

export function api_getVoteList() {
    return request("/vote", { method: "GET" });
}

export function api_getVoteDetail(id) {
    return request("/vote/"+id, { method: "GET" });
}

export function api_castVote(roomId, optionId) {
    return request("/vote/"+roomId+"/cast", {
        method: "POST",
        body: JSON.stringify({optionId}),
    });
}

export function api_updateVote(roomId, optionId) {
    return request("/vote/"+roomId+"/cast", {
        method: "PUT",
        body: JSON.stringify({optionId}),
    });
}

export function api_createVoteRoom(voteRoomData) {
    return request("/vote", {
        method: "POST",
        body: JSON.stringify(voteRoomData),
    });
}

export function api_test() {
    return request("", { method: "GET" });
}