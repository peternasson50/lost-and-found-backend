const API_BASE_URL = "http://localhost:8080/api";

function saveToken(token) {
    localStorage.setItem("token", token);
}

function getToken() {
    return localStorage.getItem("token");
}

function clearToken() {
    localStorage.removeItem("token");
}

function isLoggedIn() {
    return getToken() !== null;
}

async function apiRequest(endpoint, method = "GET", body = null) {
    const headers = {
        "Content-Type": "application/json"
    };

    const token = getToken();
    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    const options = {
        method: method,
        headers: headers
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(API_BASE_URL + endpoint, options);

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Request failed with status " + response.status);
    }

    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
        return await response.json();
    }
    return null;
}

function requireLogin() {
    if (!isLoggedIn()) {
        window.location.href = "index.html";
    }
}

function logout() {
    clearToken();
    localStorage.removeItem("userRole");
    localStorage.removeItem("userName");
    window.location.href = "index.html";
}