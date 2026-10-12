/**
 * PetroFlow Suppliers Module
 */
const SuppliersModule = {
    suppliers: [],

    async load() {
        try {
            this.suppliers = await API.getSuppliers();
            this.renderTable(this.suppliers);
        } catch (err) {
            showToast('Failed to load suppliers: ' + err.message, 'error');
        }
    },

    renderTable(list) {
        const tbody = document.getElementById('suppliers-table-tbody');
        if (!tbody) return;
        tbody.innerHTML = '';

        list.forEach(sup => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><strong style="color: #a855f7;">${sup.supplierCode}</strong></td>
                <td><strong>${sup.name}</strong></td>
                <td>${sup.phone}</td>
                <td>${sup.email || 'N/A'}</td>
                <td>${sup.address || 'N/A'}</td>
                <td><span class="badge badge-active">${sup.status}</span></td>
            `;
            tbody.appendChild(tr);
        });
    },

    async createSupplier() {
        const payload = {
            name: document.getElementById('add-sup-name').value,
            phone: document.getElementById('add-sup-phone').value,
            email: document.getElementById('add-sup-email').value,
            address: document.getElementById('add-sup-address').value,
            status: 'ACTIVE'
        };

        if (!payload.name || !payload.phone) {
            showToast('Name and phone are required', 'warning');
            return;
        }

        try {
            const created = await API.createSupplier(payload);
            showToast(`Supplier ${created.supplierCode} registered`, 'success');
            closeModal('add-supplier-modal');
            this.load();
        } catch (err) {
            showToast('Failed to add supplier: ' + err.message, 'error');
        }
    }
};
