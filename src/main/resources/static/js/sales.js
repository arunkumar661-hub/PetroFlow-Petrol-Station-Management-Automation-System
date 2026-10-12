/**
 * PetroFlow Sales Module
 */
const SalesModule = {
    sales: [],

    async load() {
        try {
            this.sales = await API.getSales();
            this.renderTable(this.sales);
        } catch (err) {
            showToast('Failed to load sales history: ' + err.message, 'error');
        }
    },

    renderTable(list) {
        const tbody = document.getElementById('sales-table-tbody');
        if (!tbody) return;
        tbody.innerHTML = '';

        if (!list || list.length === 0) {
            tbody.innerHTML = '<tr><td colspan="10" class="empty-state"><i class="fas fa-receipt"></i><br>No sales records found</td></tr>';
            return;
        }

        list.forEach(s => {
            const tr = document.createElement('tr');
            const dateStr = new Date(s.saleDate).toLocaleString('en-GB', { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' });
            tr.innerHTML = `
                <td><strong style="color: #60a5fa;">${s.transactionNumber}</strong></td>
                <td>${dateStr}</td>
                <td><span class="badge badge-active">${s.pumpCode}</span></td>
                <td><strong>${s.fuelName}</strong></td>
                <td>${s.employeeName} (${s.employeeCode})</td>
                <td>${s.quantity}</td>
                <td>${s.unit}</td>
                <td>₹${s.pricePerUnit}</td>
                <td><strong style="color: #38bdf8;">₹${s.totalAmount}</strong></td>
                <td><span class="badge badge-completed">COMPLETED</span></td>
                <td>
                    <button class="btn btn-secondary btn-sm" onclick="BillingModule.viewInvoiceByTxn('${s.transactionNumber}')">
                        <i class="fas fa-file-invoice"></i> Invoice
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    },

    filter() {
        const query = document.getElementById('sales-search-input')?.value.toLowerCase() || '';
        const filtered = this.sales.filter(s =>
            s.transactionNumber.toLowerCase().includes(query) ||
            s.pumpCode.toLowerCase().includes(query) ||
            s.fuelName.toLowerCase().includes(query) ||
            s.employeeName.toLowerCase().includes(query)
        );
        this.renderTable(filtered);
    }
};
