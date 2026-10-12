/**
 * PetroFlow Inventory Tank Module
 */
const InventoryModule = {
    inventory: [],

    async load() {
        try {
            this.inventory = await API.getInventory();
            this.renderTable(this.inventory);
        } catch (err) {
            showToast('Failed to load inventory tanks: ' + err.message, 'error');
        }
    },

    renderTable(list) {
        const tbody = document.getElementById('inventory-table-tbody');
        if (!tbody) return;
        tbody.innerHTML = '';

        list.forEach(inv => {
            const tr = document.createElement('tr');
            const dateStr = new Date(inv.lastUpdated).toLocaleString('en-GB', { day: '2-digit', month: 'short', hour: '2-digit', minute: '2-digit' });
            const statusBadge = inv.lowStock 
                ? '<span class="badge badge-low-stock"><i class="fas fa-exclamation-triangle"></i> LOW STOCK</span>' 
                : '<span class="badge badge-good"><i class="fas fa-check"></i> GOOD</span>';

            tr.innerHTML = `
                <td><strong style="color: #38bdf8;">${inv.fuelCode}</strong></td>
                <td><strong>${inv.fuelName}</strong></td>
                <td><strong style="font-size: 1.05rem; color: ${inv.lowStock ? '#ef4444' : '#22c55e'};">${inv.currentQuantity}</strong></td>
                <td><span class="badge badge-active">${inv.unit}</span></td>
                <td>${inv.minimumStockLevel} ${inv.unit}</td>
                <td>${statusBadge}</td>
                <td><span style="color: var(--text-muted); font-size: 0.8rem;">${dateStr}</span></td>
                <td>
                    <button class="btn btn-warning btn-sm" onclick="InventoryModule.openRestockModal('${inv.fuelCode}', '${inv.fuelName}', '${inv.unit}')">
                        <i class="fas fa-plus-circle"></i> Restock
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    },

    openRestockModal(code, name, unit) {
        document.getElementById('restock-fuel-code').value = code;
        document.getElementById('restock-fuel-title').innerText = `${name} (${code})`;
        document.getElementById('restock-unit-label').innerText = unit;
        openModal('restock-modal');
    },

    async submitRestock() {
        const payload = {
            fuelCode: document.getElementById('restock-fuel-code').value,
            quantity: parseFloat(document.getElementById('restock-qty').value),
            action: document.getElementById('restock-action').value,
            notes: document.getElementById('restock-notes').value
        };

        if (!payload.quantity || payload.quantity <= 0) {
            showToast('Please enter a valid stock quantity', 'warning');
            return;
        }

        try {
            await API.adjustStock(payload);
            showToast(`Inventory for ${payload.fuelCode} updated successfully`, 'success');
            closeModal('restock-modal');
            this.load();
        } catch (err) {
            showToast('Stock adjustment failed: ' + err.message, 'error');
        }
    }
};
