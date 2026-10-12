/**
 * PetroFlow Analytics & Reports Module
 */
const ReportsModule = {
    async load() {
        this.applyFilter();
    },

    async applyFilter() {
        const params = {
            startDate: document.getElementById('rpt-start-date')?.value || '',
            endDate: document.getElementById('rpt-end-date')?.value || '',
            fuelCode: document.getElementById('rpt-fuel-select')?.value || '',
            pumpCode: document.getElementById('rpt-pump-select')?.value || '',
            employeeCode: document.getElementById('rpt-emp-select')?.value || ''
        };

        // Clean empty keys
        Object.keys(params).forEach(k => !params[k] && delete params[k]);

        try {
            const report = await API.getSalesReport(params);
            this.renderSummary(report);
            this.renderSales(report.sales);
        } catch (err) {
            showToast('Failed to generate report: ' + err.message, 'error');
        }
    },

    renderSummary(report) {
        document.getElementById('rpt-total-txns').innerText = report.totalTransactions || 0;
        document.getElementById('rpt-total-revenue').innerText = '₹' + Number(report.totalRevenue || 0).toLocaleString('en-IN', { minimumFractionDigits: 2 });
        document.getElementById('rpt-total-volume').innerText = Number(report.totalQuantityDispensed || 0).toLocaleString('en-IN') + ' Units';
    },

    renderSales(sales) {
        const tbody = document.getElementById('rpt-sales-table-tbody');
        if (!tbody) return;
        tbody.innerHTML = '';

        if (!sales || sales.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="empty-state"><i class="fas fa-chart-bar"></i><br>No matching records for chosen filters</td></tr>';
            return;
        }

        sales.forEach(s => {
            const tr = document.createElement('tr');
            const dateStr = new Date(s.saleDate).toLocaleString('en-GB', { day: '2-digit', month: 'short', hour: '2-digit', minute: '2-digit' });
            tr.innerHTML = `
                <td><strong style="color: #60a5fa;">${s.transactionNumber}</strong></td>
                <td>${dateStr}</td>
                <td>${s.pumpCode}</td>
                <td>${s.fuelName}</td>
                <td>${s.employeeName}</td>
                <td>${s.quantity} ${s.unit}</td>
                <td><strong style="color: #38bdf8;">₹${s.totalAmount}</strong></td>
            `;
            tbody.appendChild(tr);
        });
    },

    resetFilters() {
        document.getElementById('rpt-start-date').value = '';
        document.getElementById('rpt-end-date').value = '';
        document.getElementById('rpt-fuel-select').value = '';
        document.getElementById('rpt-pump-select').value = '';
        document.getElementById('rpt-emp-select').value = '';
        this.applyFilter();
    }
};
