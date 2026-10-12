/**
 * PetroFlow Unified API Client
 */
const API = {
    async request(url, options = {}) {
        try {
            const config = {
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    ...options.headers
                },
                ...options
            };
            const response = await fetch(url, config);
            const result = await response.json();

            if (!response.ok || !result.success) {
                const errorMsg = result.message || 'Server returned an error';
                console.error('API Error:', result);
                throw new Error(errorMsg);
            }
            return result.data;
        } catch (err) {
            console.error('Network/Request Failure:', err);
            throw err;
        }
    },

    get(url) {
        return this.request(url, { method: 'GET' });
    },

    post(url, body) {
        return this.request(url, {
            method: 'POST',
            body: JSON.stringify(body)
        });
    },

    put(url, body) {
        return this.request(url, {
            method: 'PUT',
            body: JSON.stringify(body)
        });
    },

    // Specific endpoints
    getDashboard: () => API.get('/api/dashboard'),
    getFuels: (activeOnly = false) => API.get(`/api/fuels?activeOnly=${activeOnly}`),
    createFuel: (fuel) => API.post('/api/fuels', fuel),
    updateFuel: (code, fuel) => API.put(`/api/fuels/${code}`, fuel),

    getInventory: () => API.get('/api/inventory'),
    adjustStock: (adj) => API.post('/api/inventory/adjust', adj),

    getPumps: () => API.get('/api/pumps'),
    createPump: (pump) => API.post('/api/pumps', pump),
    updatePumpStatus: (code, status) => API.put(`/api/pumps/${code}/status?status=${status}`, {}),

    getEmployees: (activeOnly = false) => API.get(`/api/employees?activeOnly=${activeOnly}`),
    createEmployee: (emp) => API.post('/api/employees', emp),

    getSuppliers: () => API.get('/api/suppliers'),
    createSupplier: (sup) => API.post('/api/suppliers', sup),

    dispenseFuel: (req) => API.post('/api/pump-transactions/dispense', req),
    getTransactions: () => API.get('/api/pump-transactions'),

    getSales: () => API.get('/api/sales'),
    getInvoices: () => API.get('/api/invoices'),
    getInvoice: (invNo) => API.get(`/api/invoices/${invNo}`),
    getSalesReport: (params) => {
        const query = new URLSearchParams(params).toString();
        return API.get(`/api/reports/sales?${query}`);
    }
};
