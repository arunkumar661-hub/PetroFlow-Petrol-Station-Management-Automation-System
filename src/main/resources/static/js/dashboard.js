/**
 * PetroFlow Dashboard Module
 */
const DashboardModule = {
    revenueChart: null,
    fuelChart: null,

    async load() {
        try {
            const data = await API.getDashboard();
            this.renderKPIs(data);
            this.renderAlerts(data.inventoryStatus);
            this.renderCharts(data);
            this.renderRecentSales(data.recentSales);
        } catch (err) {
            showToast('Failed to load dashboard metrics: ' + err.message, 'error');
        }
    },

    renderKPIs(data) {
        document.getElementById('kpi-sales-count').innerText = data.todaySalesCount;
        document.getElementById('kpi-today-revenue').innerText = '₹' + Number(data.todayRevenue).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
        document.getElementById('kpi-fuel-dispensed').innerText = Number(data.todayFuelDispensedLiters).toLocaleString('en-IN', { maximumFractionDigits: 1 }) + ' L';
        document.getElementById('kpi-active-pumps').innerText = data.activePumpsRatio;
    },

    renderAlerts(inventoryList) {
        const container = document.getElementById('dashboard-alerts-container');
        if (!container) return;
        container.innerHTML = '';

        const lowStockItems = (inventoryList || []).filter(i => i.lowStock);
        if (lowStockItems.length > 0) {
            const names = lowStockItems.map(i => `${i.fuelName} (${i.currentQuantity} ${i.unit})`).join(', ');
            const banner = document.createElement('div');
            banner.className = 'alert-banner';
            banner.innerHTML = `
                <div class="alert-content">
                    <i class="fas fa-exclamation-triangle"></i>
                    <div>
                        <div class="alert-title">CRITICAL STOCK WARNING</div>
                        <div class="alert-desc">Fuel tank level below safe operational threshold: <strong>${names}</strong>. Please restock immediately.</div>
                    </div>
                </div>
                <button class="btn btn-warning btn-sm" onclick="switchView('inventory')"><i class="fas fa-gas-pump"></i> Restock Now</button>
            `;
            container.appendChild(banner);
        }
    },

    renderCharts(data) {
        // 1. Revenue Trend Line Chart
        const revCtx = document.getElementById('revenueTrendChart')?.getContext('2d');
        if (revCtx) {
            const labels = Object.keys(data.last7DaysRevenue || {});
            const values = Object.values(data.last7DaysRevenue || {});

            if (this.revenueChart) this.revenueChart.destroy();
            this.revenueChart = new Chart(revCtx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Daily Revenue (₹)',
                        data: values,
                        borderColor: '#38bdf8',
                        backgroundColor: 'rgba(56, 189, 248, 0.15)',
                        borderWidth: 3,
                        fill: true,
                        tension: 0.35,
                        pointBackgroundColor: '#0284c7',
                        pointRadius: 4
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: { legend: { display: false } },
                    scales: {
                        x: { grid: { color: 'rgba(255,255,255,0.05)' }, ticks: { color: '#94a3b8' } },
                        y: { grid: { color: 'rgba(255,255,255,0.05)' }, ticks: { color: '#94a3b8' } }
                    }
                }
            });
        }

        // 2. Fuel Breakdown Doughnut Chart
        const fuelCtx = document.getElementById('fuelWiseChart')?.getContext('2d');
        if (fuelCtx) {
            const labels = Object.keys(data.fuelWiseSales || {});
            const values = Object.values(data.fuelWiseSales || {});

            if (this.fuelChart) this.fuelChart.destroy();
            this.fuelChart = new Chart(fuelCtx, {
                type: 'doughnut',
                data: {
                    labels: labels.length > 0 ? labels : ['No Sales Yet'],
                    datasets: [{
                        data: values.length > 0 ? values : [1],
                        backgroundColor: ['#f59e0b', '#3b82f6', '#10b981', '#a855f7', '#ec4899'],
                        borderColor: '#0f1c38',
                        borderWidth: 2
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: { position: 'bottom', labels: { color: '#94a3b8', font: { size: 11 } } }
                    }
                }
            });
        }
    },

    renderRecentSales(sales) {
        const tbody = document.getElementById('dashboard-recent-sales-tbody');
        if (!tbody) return;
        tbody.innerHTML = '';

        if (!sales || sales.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="empty-state"><i class="fas fa-receipt"></i><br>No sales recorded yet today</td></tr>';
            return;
        }

        sales.forEach(s => {
            const tr = document.createElement('tr');
            const dateStr = new Date(s.saleDate).toLocaleTimeString('en-GB', { hour: '2-digit', minute: '2-digit' });
            tr.innerHTML = `
                <td><strong style="color: #60a5fa;">${s.transactionNumber}</strong></td>
                <td><span class="badge badge-active">${s.pumpCode}</span></td>
                <td>${s.fuelName}</td>
                <td><strong>${s.quantity} ${s.unit}</strong></td>
                <td>₹${s.pricePerUnit}</td>
                <td><strong style="color: #38bdf8;">₹${s.totalAmount}</strong></td>
                <td><span style="color: var(--text-muted); font-size: 0.8rem;">${dateStr}</span></td>
            `;
            tbody.appendChild(tr);
        });
    }
};
