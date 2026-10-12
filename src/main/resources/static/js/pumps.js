/**
 * PetroFlow Forecourt Pump Monitor & Management
 */
const PumpsModule = {
    pumps: [],

    async load() {
        try {
            this.pumps = await API.getPumps();
            this.renderGrid();
        } catch (err) {
            showToast('Failed to load pump monitors: ' + err.message, 'error');
        }
    },

    renderGrid() {
        const grid = document.getElementById('pumps-grid');
        if (!grid) return;
        grid.innerHTML = '';

        if (this.pumps.length === 0) {
            grid.innerHTML = '<div class="empty-state"><i class="fas fa-gas-pump"></i><p>No pumps configured</p></div>';
            return;
        }

        this.pumps.forEach(p => {
            const card = document.createElement('div');
            card.className = 'kpi-card';
            card.style.flexDirection = 'column';
            card.style.alignItems = 'stretch';
            card.style.gap = '14px';

            const statusClass = p.status === 'ONLINE' ? 'badge-online' : (p.status === 'DISPENSING' ? 'badge-dispensing' : 'badge-maintenance');

            card.innerHTML = `
                <div style="display: flex; justify-content: space-between; align-items: center;">
                    <div>
                        <strong style="font-size: 1.15rem; color: #38bdf8;">${p.pumpCode}</strong>
                        <div style="font-size: 0.8rem; color: var(--text-secondary);">${p.pumpName}</div>
                    </div>
                    <span class="badge ${statusClass}">${p.status}</span>
                </div>
                <div style="background: var(--bg-surface); padding: 12px; border-radius: 8px; font-size: 0.85rem; display: flex; flex-direction: column; gap: 6px;">
                    <div style="display: flex; justify-content: space-between;">
                        <span style="color: var(--text-muted);">Fuel:</span>
                        <strong>${p.supportedFuelName}</strong>
                    </div>
                    <div style="display: flex; justify-content: space-between;">
                        <span style="color: var(--text-muted);">Price:</span>
                        <strong>₹${p.fuelPrice} / ${p.fuelUnit}</strong>
                    </div>
                    <div style="display: flex; justify-content: space-between;">
                        <span style="color: var(--text-muted);">Last Txn:</span>
                        <span style="color: #60a5fa; font-weight: 600;">${p.lastTransactionNumber || 'None'}</span>
                    </div>
                </div>
                <div style="display: flex; gap: 8px;">
                    <button class="btn btn-primary btn-sm" style="flex: 1;" onclick="PumpsModule.openSimulatorWithPump('${p.pumpCode}')">
                        <i class="fas fa-play"></i> Open Dispenser
                    </button>
                    <button class="btn btn-secondary btn-sm" onclick="PumpsModule.openStatusModal('${p.pumpCode}', '${p.status}')">
                        <i class="fas fa-cog"></i>
                    </button>
                </div>
            `;
            grid.appendChild(card);
        });
    },

    openSimulatorWithPump(pumpCode) {
        switchView('simulator');
        const select = document.getElementById('sim-pump-select');
        if (select) {
            select.value = pumpCode;
            SimulatorModule.onPumpSelected(pumpCode);
        }
    },

    openStatusModal(pumpCode, currentStatus) {
        document.getElementById('status-pump-code').value = pumpCode;
        document.getElementById('status-pump-select').value = currentStatus;
        openModal('pump-status-modal');
    },

    async saveStatus() {
        const code = document.getElementById('status-pump-code').value;
        const status = document.getElementById('status-pump-select').value;
        try {
            await API.updatePumpStatus(code, status);
            showToast(`Pump ${code} status updated to ${status}`, 'success');
            closeModal('pump-status-modal');
            this.load();
        } catch (err) {
            showToast('Failed to update status: ' + err.message, 'error');
        }
    },

    async createPump() {
        const payload = {
            pumpName: document.getElementById('add-pump-name').value,
            supportedFuelCode: document.getElementById('add-pump-fuel').value,
            status: document.getElementById('add-pump-status').value
        };

        try {
            const created = await API.createPump(payload);
            showToast(`Dispenser ${created.pumpCode} created successfully`, 'success');
            closeModal('add-pump-modal');
            this.load();
        } catch (err) {
            showToast('Failed to create pump: ' + err.message, 'error');
        }
    }
};
