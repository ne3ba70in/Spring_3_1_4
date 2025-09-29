document.addEventListener("DOMContentLoaded", () => {
    const navbarUsername = document.getElementById("navbar-username");
    const currentUserTableBody = document.getElementById("currentUserTableBody");

    async function loadCurrentUser() {
        try {
            const res = await fetch("/api/users/current");
            if (!res.ok) {
                console.error("Ошибка получения пользователя:", res.status);
                return;
            }

            const user = await res.json();

            navbarUsername.innerHTML = `
                <strong>${user.email}</strong>
                <span class="fw-light"> with roles: </span>
                <strong>${user.roles.map(r => r.name.replace("ROLE_", "")).join(", ")}</strong>
            `;

            // Таблица
            currentUserTableBody.innerHTML = `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.roles.map(r => r.name.replace("ROLE_", "")).join(", ")}</td>
                </tr>
            `;
        } catch (err) {
            console.error(err);
        }
    }

    loadCurrentUser();
});
