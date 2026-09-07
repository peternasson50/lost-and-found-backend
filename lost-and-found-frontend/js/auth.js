function toggleForms() {
    const login = document.getElementById("login-section");
    const register = document.getElementById("register-section");
    login.style.display = login.style.display === "none" ? "block" : "none";
    register.style.display = register.style.display === "none" ? "block" : "none";
}

async function handleLogin() {
    const email = document.getElementById("login-email").value;
    const password = document.getElementById("login-password").value;
    const errorEl = document.getElementById("login-error");
    errorEl.textContent = "";

    try {
        const result = await apiRequest("/auth/login", "POST", { email, password });
        saveToken(result.token);
        localStorage.setItem("userRole", result.role);
        localStorage.setItem("userName", result.fullName);

        if (result.role === "ADMIN") {
            window.location.href = "admin-dashboard.html";
        } else {
            window.location.href = "dashboard.html";
        }
    } catch (err) {
        errorEl.textContent = "Login failed: " + err.message;
    }
}

async function handleRegister() {
    const fullName = document.getElementById("register-name").value;
    const email = document.getElementById("register-email").value;
    const password = document.getElementById("register-password").value;
    const studentId = document.getElementById("register-studentid").value;
    const errorEl = document.getElementById("register-error");
    errorEl.textContent = "";

    try {
        const result = await apiRequest("/auth/register", "POST", { fullName, email, password, studentId });
        saveToken(result.token);
        localStorage.setItem("userRole", result.role);
        localStorage.setItem("userName", result.fullName);
        window.location.href = "dashboard.html";
    } catch (err) {
        errorEl.textContent = "Registration failed: " + err.message;
    }
}