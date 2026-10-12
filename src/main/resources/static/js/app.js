/**
 * PetroFlow Core App - Navigation, Clock, Modals, Toasts
 */

// Live Clock
function startLiveClock() {
    const clockEl = document.getElementById('live-clock');
    if (!clockEl) return;
    const update = () => {
        const now = new Date();
        clockEl.innerText = now.toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' }) + ' ' +
                            now.toLocaleTimeString('en-GB', { hour: '2-digit', minute: '2-digit', second: '2-digit' });
    };
    update();
    setInterval(update, 1000);
}

// Toast Notification
function showToast(message, type = 'success') {
    const container = document.getElementById('toast-container');
    if (!container) return;

    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    const icon = type === 'success' ? 'fa-check-circle' : (type === 'warning' ? 'fa-exclamation-triangle' : 'fa-times-circle');
    toast.innerHTML = `<i class="fas ${icon}"></i> <span>${message}</span>`;

    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}

// Modal Management
function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('show');
    }
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('show');
    }
}

// View Routing
function switchView(viewName) {
    document.querySelectorAll('.view-section').forEach(sec => sec.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(item => item.classList.remove('active'));

    const targetSection = document.getElementById(`view-${viewName}`);
    const targetNav = document.querySelector(`.nav-item[data-view="${viewName}"]`);

    if (targetSection) targetSection.classList.add('active');
    if (targetNav) targetNav.classList.add('active');

    const titleMap = {
        'dashboard': { title: 'Operational Dashboard', desc: 'Real-time forecourt telemetry and sales performance' },
        'monitor': { title: 'Forecourt Pump Monitor', desc: 'Live dispenser status and island activity' },
        'simulator': { title: 'Dispenser Pump Simulator', desc: 'Interactive software fuel dispensing unit' },
        'sales': { title: 'Sales Ledger', desc: 'Comprehensive transaction history and sales records' },
        'billing': { title: 'Invoices & Billing', desc: 'Tax invoice generation and receipt printing' },
        'fuels': { title: 'Fuel Master', desc: 'Fuel product codes, units, and real-time pricing' },
        'inventory': { title: 'Fuel Inventory Tanks', desc: 'Stock level tracking and automatic low-stock alerts' },
        'employees': { title: 'Station Staff', desc: 'Attendants, supervisors, and forecourt staff' },
        'suppliers': { title: 'Fuel Suppliers', desc: 'Terminal depots and authorized petroleum suppliers' },
        'reports': { title: 'Analytics & Reports', desc: 'Custom date range financial and operational reports' }
    };

    const info = titleMap[viewName] || { title: 'PetroFlow System', desc: 'Station Management' };
    document.getElementById('page-heading').innerText = info.title;
    document.getElementById('page-subheading').innerText = info.desc;

    // Trigger section refresh
    if (viewName === 'dashboard') DashboardModule.load();
    if (viewName === 'monitor') PumpsModule.load();
    if (viewName === 'simulator') SimulatorModule.init();
    if (viewName === 'sales') SalesModule.load();
    if (viewName === 'billing') BillingModule.load();
    if (viewName === 'fuels') FuelsModule.load();
    if (viewName === 'inventory') InventoryModule.load();
    if (viewName === 'employees') EmployeesModule.load();
    if (viewName === 'suppliers') SuppliersModule.load();
    if (viewName === 'reports') ReportsModule.load();
}

// Bootstrap on DOM Ready
document.addEventListener('DOMContentLoaded', () => {
    startLiveClock();

    // Nav Click Listeners
    document.querySelectorAll('.nav-item').forEach(item => {
        item.addEventListener('click', (e) => {
            const view = item.getAttribute('data-view');
            if (view) switchView(view);
        });
    });

    // Close modals on clicking backdrop or close buttons
    document.querySelectorAll('.modal-backdrop').forEach(modal => {
        modal.addEventListener('click', (e) => {
            if (e.target === modal) modal.classList.remove('show');
        });
    });
    document.querySelectorAll('.modal-close, .btn-modal-cancel').forEach(btn => {
        btn.addEventListener('click', () => {
            btn.closest('.modal-backdrop').classList.remove('show');
        });
    });

    // Initial load
    switchView('dashboard');
});
