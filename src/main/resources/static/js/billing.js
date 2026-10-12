/**
 * PetroFlow Billing & Invoice Module
 */
const BillingModule = {
    invoices: [],

    async load() {
        try {
            this.invoices = await API.getInvoices();
            this.renderTable(this.invoices);
        } catch (err) {
            showToast('Failed to load invoices: ' + err.message, 'error');
        }
    },

    renderTable(list) {
        const tbody = document.getElementById('invoices-table-tbody');
        if (!tbody) return;
        tbody.innerHTML = '';

        if (!list || list.length === 0) {
            tbody.innerHTML = '<tr><td colspan="9" class="empty-state"><i class="fas fa-file-invoice-dollar"></i><br>No invoices generated yet</td></tr>';
            return;
        }

        list.forEach(inv => {
            const tr = document.createElement('tr');
            const dateStr = new Date(inv.invoiceDate).toLocaleString('en-GB', { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' });
            tr.innerHTML = `
                <td><strong style="color: #38bdf8;">${inv.invoiceNumber}</strong></td>
                <td><strong style="color: #60a5fa;">${inv.transactionNumber}</strong></td>
                <td>${dateStr}</td>
                <td>${inv.customerName}</td>
                <td>${inv.vehicleNumber || 'N/A'}</td>
                <td>${inv.fuelName} (${inv.quantity} ${inv.unit})</td>
                <td><strong style="color: #22c55e;">₹${inv.totalAmount}</strong></td>
                <td><span class="badge badge-active">${inv.paymentMethod}</span></td>
                <td>
                    <button class="btn btn-primary btn-sm" onclick="BillingModule.viewInvoice('${inv.invoiceNumber}')">
                        <i class="fas fa-print"></i> View & Print
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    },

    async viewInvoice(invNo) {
        try {
            const inv = await API.getInvoice(invNo);
            this.renderInvoiceModal(inv);
            openModal('invoice-modal');
        } catch (err) {
            showToast('Failed to fetch invoice: ' + err.message, 'error');
        }
    },

    async viewInvoiceByTxn(txnNo) {
        try {
            const res = await API.request(`/api/invoices/transaction/${txnNo}`);
            this.renderInvoiceModal(res);
            openModal('invoice-modal');
        } catch (err) {
            showToast('Invoice lookup failed: ' + err.message, 'error');
        }
    },

    renderInvoiceModal(inv) {
        const dateStr = new Date(inv.invoiceDate).toLocaleString('en-GB', { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' });
        const modalBody = document.getElementById('invoice-render-box');
        if (!modalBody) return;

        modalBody.innerHTML = `
            <div class="invoice-container">
                <div class="invoice-header">
                    <div class="invoice-logo">
                        <h2>PETROFLOW</h2>
                        <p>Petrol Station Management & Automation</p>
                        <p style="font-size: 0.75rem; color: #94a3b8;">Highway Forecourt Terminal Hub • 24x7 Service</p>
                    </div>
                    <div class="invoice-meta">
                        <div class="inv-no">${inv.invoiceNumber}</div>
                        <div class="inv-date">Date: ${dateStr}</div>
                        <div style="font-size: 0.8rem; font-weight: 600; color: #475569;">Txn: ${inv.transactionNumber}</div>
                    </div>
                </div>

                <div class="invoice-details-grid">
                    <div>
                        <strong>Billed To:</strong><br>
                        ${inv.customerName}<br>
                        Vehicle: <strong>${inv.vehicleNumber || 'Walk-in'}</strong>
                    </div>
                    <div>
                        <strong>Dispenser Point:</strong><br>
                        Dispenser: <strong>${inv.pumpCode}</strong><br>
                        Payment: <strong>${inv.paymentMethod}</strong>
                    </div>
                </div>

                <table class="invoice-table">
                    <thead>
                        <tr>
                            <th>Description</th>
                            <th>Qty</th>
                            <th>Unit Price</th>
                            <th style="text-align: right;">Amount</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><strong>${inv.fuelName}</strong></td>
                            <td>${inv.quantity} ${inv.unit}</td>
                            <td>₹${Number(inv.pricePerUnit).toFixed(2)}</td>
                            <td style="text-align: right; font-weight: 700;">₹${Number(inv.subtotal).toFixed(2)}</td>
                        </tr>
                    </tbody>
                </table>

                <div class="invoice-summary-box">
                    <div class="inv-row">
                        <span>Subtotal:</span>
                        <span>₹${Number(inv.subtotal).toFixed(2)}</span>
                    </div>
                    <div class="inv-row">
                        <span>GST / Service Tax:</span>
                        <span>₹${Number(inv.taxAmount || 0).toFixed(2)}</span>
                    </div>
                    <div class="inv-row total-row">
                        <span>Total Paid:</span>
                        <span>₹${Number(inv.totalAmount).toFixed(2)}</span>
                    </div>
                </div>

                <div class="invoice-footer">
                    Thank you for fueling with PetroFlow! Have a safe and pleasant journey.
                    <br><span style="font-size: 0.7rem;">Automated System Generated Invoice • No signature required</span>
                </div>
            </div>
        `;
    },

    printCurrentInvoice() {
        window.print();
    }
};
