/**
 * PetroFlow Staff Management
 */
const EmployeesModule = {
    employees: [],

    async load() {
        try {
            this.employees = await API.getEmployees();
            this.renderTable(this.employees);
        } catch (err) {
            showToast('Failed to load staff directory: ' + err.message, 'error');
        }
    },

    renderTable(list) {
        const tbody = document.getElementById('employees-table-tbody');
        if (!tbody) return;
        tbody.innerHTML = '';

        list.forEach(emp => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><strong style="color: #60a5fa;">${emp.employeeCode}</strong></td>
                <td><strong>${emp.name}</strong></td>
                <td><span class="badge badge-active">${emp.role}</span></td>
                <td>${emp.phone}</td>
                <td>${emp.email || 'N/A'}</td>
                <td>₹${Number(emp.salary).toLocaleString('en-IN')}</td>
                <td><span class="badge ${emp.status === 'ACTIVE' ? 'badge-active' : 'badge-inactive'}">${emp.status}</span></td>
                <td>${emp.joiningDate || 'N/A'}</td>
            `;
            tbody.appendChild(tr);
        });
    },

    async createEmployee() {
        const payload = {
            name: document.getElementById('add-emp-name').value,
            phone: document.getElementById('add-emp-phone').value,
            email: document.getElementById('add-emp-email').value,
            role: document.getElementById('add-emp-role').value,
            salary: parseFloat(document.getElementById('add-emp-salary').value) || 0,
            status: 'ACTIVE'
        };

        if (!payload.name || !payload.phone) {
            showToast('Name and phone are required', 'warning');
            return;
        }

        try {
            const created = await API.createEmployee(payload);
            showToast(`Employee ${created.employeeCode} added`, 'success');
            closeModal('add-employee-modal');
            this.load();
        } catch (err) {
            showToast('Failed to add employee: ' + err.message, 'error');
        }
    }
};
