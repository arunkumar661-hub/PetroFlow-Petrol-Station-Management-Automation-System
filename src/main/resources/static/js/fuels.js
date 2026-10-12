/**
 * PetroFlow Fuels Module
 */
const FuelsModule = {
    fuels: [],

    async load() {
        try {
            this.fuels = await API.getFuels();
            this.renderTable(this.fuels);
        } catch (err) {
            showToast('Failed to load fuels: ' + err.message, 'error');
        }
    },

    renderTable(list) {
        const tbody = document.getElementById('fuels-table-tbody');
        if (!tbody) return;
        tbody.innerHTML = '';

        list.forEach(f => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td><strong style="color: #f59e0b; font-size: 0.95rem;">${f.fuelCode}</strong></td>
                <td><strong>${f.fuelName}</strong></td>
                <td><strong style="color: #38bdf8;">₹${f.pricePerUnit}</strong></td>
                <td><span class="badge badge-active">${f.unit}</span></td>
                <td><span class="badge ${f.active ? 'badge-active' : 'badge-inactive'}">${f.active ? 'Active' : 'Inactive'}</span></td>
                <td>
                    <button class="btn btn-secondary btn-sm" onclick="FuelsModule.openEditModal('${f.fuelCode}', '${f.fuelName}', ${f.pricePerUnit}, '${f.unit}', ${f.active})">
                        <i class="fas fa-edit"></i> Edit Price
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    },

    openEditModal(code, name, price, unit, active) {
        document.getElementById('edit-fuel-code').value = code;
        document.getElementById('edit-fuel-name').value = name;
        document.getElementById('edit-fuel-price').value = price;
        document.getElementById('edit-fuel-unit').value = unit;
        document.getElementById('edit-fuel-active').checked = active;
        openModal('edit-fuel-modal');
    },

    async saveFuel() {
        const code = document.getElementById('edit-fuel-code').value;
        const payload = {
            fuelName: document.getElementById('edit-fuel-name').value,
            pricePerUnit: parseFloat(document.getElementById('edit-fuel-price').value),
            unit: document.getElementById('edit-fuel-unit').value,
            active: document.getElementById('edit-fuel-active').checked
        };

        try {
            await API.updateFuel(code, payload);
            showToast(`Fuel ${code} updated successfully`, 'success');
            closeModal('edit-fuel-modal');
            this.load();
        } catch (err) {
            showToast('Failed to update fuel: ' + err.message, 'error');
        }
    },

    async createFuel() {
        const payload = {
            fuelCode: document.getElementById('add-fuel-code').value,
            fuelName: document.getElementById('add-fuel-name').value,
            pricePerUnit: parseFloat(document.getElementById('add-fuel-price').value),
            unit: document.getElementById('add-fuel-unit').value,
            active: true
        };

        try {
            await API.createFuel(payload);
            showToast(`Fuel ${payload.fuelCode} registered`, 'success');
            closeModal('add-fuel-modal');
            this.load();
        } catch (err) {
            showToast('Failed to add fuel: ' + err.message, 'error');
        }
    }
};
