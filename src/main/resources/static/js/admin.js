document.addEventListener("DOMContentLoaded", () => {
    const adminView = document.getElementById("adminView");
    const userView = document.getElementById("userView");
    const adminTabBtn = document.getElementById("adminTabBtn");
    const userTabBtn = document.getElementById("userTabBtn");

    const usersTableBody = document.getElementById("usersTableBody");
    const rolesSelect = document.getElementById("rolesSelect");
    const editRolesSelect = document.getElementById("editRolesSelect");
    const addUserForm = document.getElementById("addUserForm");
    const editUserForm = document.getElementById("editUserForm");
    const deleteUserForm = document.getElementById("deleteUserForm");
    const navbarUsername = document.getElementById("navbar-username");
    const currentUserTableBody = document.getElementById("currentUserTableBody");

    let rolesList = [];

    // --------------------- Переключение боковой панели ---------------------
    function showAdminView() {
        adminView.classList.remove("d-none");
        adminView.classList.add("d-block");
        userView.classList.add("d-none");
        userView.classList.remove("d-block");

        adminTabBtn.classList.add("active");
        userTabBtn.classList.remove("active");
    }

    function showUserView() {
        userView.classList.remove("d-none");
        userView.classList.add("d-block");
        adminView.classList.add("d-none");
        adminView.classList.remove("d-block");

        userTabBtn.classList.add("active");
        adminTabBtn.classList.remove("active");

        loadCurrentUser();
    }

    adminTabBtn.addEventListener("click", (e) => {
        e.preventDefault();
        showAdminView();
    });

    userTabBtn.addEventListener("click", (e) => {
        e.preventDefault();
        showUserView();
    });

    showAdminView();

    // --------------------- Загрузка ролей ---------------------
    async function loadRoles() {
        const res = await fetch("/api/roles");
        rolesList = await res.json();

        rolesSelect.innerHTML = "";
        editRolesSelect.innerHTML = "";

        rolesList.forEach(role => {
            const optionAdd = document.createElement("option");
            optionAdd.value = role.id;
            optionAdd.textContent = role.name.replace("ROLE_", "");
            rolesSelect.appendChild(optionAdd);

            const optionEdit = document.createElement("option");
            optionEdit.value = role.id;
            optionEdit.textContent = role.name.replace("ROLE_", "");
            editRolesSelect.appendChild(optionEdit);
        });
    }

    // --------------------- Загрузка пользователей ---------------------
    async function loadUsers() {
        const res = await fetch("/api/users");
        const users = await res.json();
        usersTableBody.innerHTML = "";

        users.forEach(user => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${user.roles.map(r => r.name.replace("ROLE_", "")).join(", ")}</td>
                <td>
                    <div class="d-flex gap-2">
                        <button class="btn btn-info btn-sm text-white editBtn" data-id="${user.id}">Edit</button>
                        <button class="btn btn-danger btn-sm text-white deleteBtn" data-id="${user.id}">Delete</button>
                    </div>
                </td>
            `;
            usersTableBody.appendChild(tr);
        });

        attachEditButtons();
        attachDeleteButtons();
    }

    // --------------------- Загрузка текущего пользователя ---------------------
    async function loadCurrentUser() {
        const res = await fetch("/api/users/current");
        if (!res.ok) return;
        const user = await res.json();

        navbarUsername.innerHTML = `
            <strong>${user.email}</strong>
            <span class="fw-light"> with roles: </span>
            <span>${user.roles.map(r => r.name.replace("ROLE_", "")).join(", ")}</span>
        `;

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
    }

    // --------------------- Добавление пользователя ---------------------
    addUserForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const formData = new FormData(addUserForm);
        const roleIds = Array.from(formData.getAll("roles")).map(Number);

        const userData = {
            firstName: formData.get("firstName"),
            lastName: formData.get("lastName"),
            age: parseInt(formData.get("age")),
            email: formData.get("email"),
            password: formData.get("password")
        };

        await fetch(`/api/users?roleIds=${roleIds.join("&roleIds=")}`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userData)
        });

        addUserForm.reset();
        loadUsers();

        // --- Переключение на вкладку Users table после добавления ---
        const usersTabLink = document.querySelector('a[href="#usersTableSection"]');
        if (usersTabLink) {
            const tab = new bootstrap.Tab(usersTabLink);
            tab.show();
        }
    });

    // --------------------- Редактирование пользователя ---------------------
    function attachEditButtons() {
        document.querySelectorAll(".editBtn").forEach(btn => {
            btn.addEventListener("click", async () => {
                const userId = btn.dataset.id;
                const res = await fetch(`/api/users/${userId}`);
                const user = await res.json();

                document.getElementById("editUserId").value = user.id;
                document.getElementById("editFirstName").value = user.firstName;
                document.getElementById("editLastName").value = user.lastName;
                document.getElementById("editAge").value = user.age;
                document.getElementById("editEmail").value = user.email;
                document.getElementById("editPassword").value = "";

                Array.from(editRolesSelect.options).forEach(opt => opt.selected = false);
                user.roles.forEach(r => {
                    const opt = Array.from(editRolesSelect.options).find(o => parseInt(o.value) === r.id);
                    if (opt) opt.selected = true;
                });

                const editModal = new bootstrap.Modal(document.getElementById("editUserModal"));
                editModal.show();
            });
        });
    }

    editUserForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const formData = new FormData(editUserForm);
        const roleIds = Array.from(editRolesSelect.selectedOptions).map(o => o.value);

        const userId = formData.get("id");
        const userData = {
            firstName: formData.get("firstName"),
            lastName: formData.get("lastName"),
            age: parseInt(formData.get("age")),
            email: formData.get("email"),
            password: formData.get("password")
        };

        await fetch(`/api/users/${userId}?roleIds=${roleIds.join("&roleIds=")}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userData)
        });

        bootstrap.Modal.getInstance(document.getElementById("editUserModal")).hide();
        loadUsers();
    });

    // --------------------- Удаление пользователя ---------------------
    function attachDeleteButtons() {
        document.querySelectorAll(".deleteBtn").forEach(btn => {
            btn.addEventListener("click", async () => {
                const userId = btn.dataset.id;
                const res = await fetch(`/api/users/${userId}`);
                const user = await res.json();

                document.getElementById("deleteUserId").value = user.id;
                document.getElementById("deleteFirstName").value = user.firstName;
                document.getElementById("deleteLastName").value = user.lastName;
                document.getElementById("deleteAge").value = user.age;
                document.getElementById("deleteEmail").value = user.email;
                document.getElementById("deleteRoles").value = user.roles.map(r => r.name.replace("ROLE_", "")).join(", ");

                const deleteModal = new bootstrap.Modal(document.getElementById("deleteUserModal"));
                deleteModal.show();
            });
        });
    }

    deleteUserForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const userId = document.getElementById("deleteUserId").value;

        const res = await fetch(`/api/users/${userId}`, { method: "DELETE" });

        if (res.ok) {
            bootstrap.Modal.getInstance(document.getElementById("deleteUserModal")).hide();
            loadUsers();
        } else {
            alert("Failed to delete user");
        }
    });

    // --------------------- Инициализация ---------------------
    loadRoles();
    loadUsers();
    loadCurrentUser();
});

