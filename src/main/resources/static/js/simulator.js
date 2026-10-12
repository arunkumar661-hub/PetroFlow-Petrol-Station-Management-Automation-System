/**
 * PetroFlow Automated Dispenser Simulator Engine
 */
const SimulatorModule = {
    pumps: [],
    fuels: [],
    employees: [],
    selectedPump: null,
    selectedFuel: null,
    currentStock: 0,
    dispenseInterval: null,
    isDispensing: false,
    dispensedVolume: 0,
    targetQuantity: 0,
    pricePerUnit: 0,

    async init() {
        try {
            const [pumpsData, fuelsData, empsData, invData] = await Promise.all([
                API.getPumps(),
                API.getFuels(true),
                API.getEmployees(true),
                API.getInventory()
            ]);

            this.pumps = pumpsData;
            this.fuels = fuelsData;
            this.employees = empsData;
            this.inventory = invData;

            this.populateDropdowns();
            this.resetSimulator();
        } catch (err) {
            showToast('Failed to initialize simulator: ' + err.message, 'error');
        }
    },

    populateDropdowns() {
        const pumpSelect = document.getElementById('sim-pump-select');
        const empSelect = document.getElementById('sim-emp-select');

        pumpSelect.innerHTML = '<option value="">-- Select Dispenser --</option>';
        this.pumps.forEach(p => {
            const opt = document.createElement('option');
            opt.value = p.pumpCode;
            opt.innerText = `${p.pumpCode} (${p.pumpName} - ${p.supportedFuelName})`;
            pumpSelect.appendChild(opt);
        });

        empSelect.innerHTML = '<option value="">-- Select Operator --</option>';
        this.employees.forEach(e => {
            const opt = document.createElement('option');
            opt.value = e.employeeCode;
            opt.innerText = `${e.employeeCode} - ${e.name} (${e.role})`;
            empSelect.appendChild(opt);
        });

        if (this.employees.length > 0) {
            empSelect.value = this.employees[0].employeeCode;
        }

        // Auto select first pump
        if (this.pumps.length > 0) {
            pumpSelect.value = this.pumps[0].pumpCode;
            this.onPumpSelected(this.pumps[0].pumpCode);
        }
    },

    onPumpSelected(pumpCode) {
        this.selectedPump = this.pumps.find(p => p.pumpCode === pumpCode);
        if (!this.selectedPump) return;

        document.getElementById('sim-dispenser-tag').innerText = this.selectedPump.pumpCode;
        document.getElementById('sim-dispenser-fuel-name').innerText = this.selectedPump.supportedFuelName;

        // Load fuel & stock
        this.selectedFuel = this.fuels.find(f => f.fuelCode === this.selectedPump.supportedFuelCode);
        const inv = (this.inventory || []).find(i => i.fuelCode === this.selectedPump.supportedFuelCode);

        this.currentStock = inv ? inv.currentQuantity : 0;
        this.pricePerUnit = this.selectedFuel ? this.selectedFuel.pricePerUnit : this.selectedPump.fuelPrice;
        const unit = this.selectedFuel ? this.selectedFuel.unit : this.selectedPump.fuelUnit;

        document.getElementById('sim-live-stock').innerText = `${this.currentStock} ${unit}`;
        document.getElementById('sim-price-display').innerText = `₹${this.pricePerUnit.toFixed(2)} / ${unit}`;
        document.getElementById('sim-disp-unit-label').innerText = `QUANTITY (${unit})`;

        this.calculateAmount();
    },

    calculateAmount() {
        const qtyInput = document.getElementById('sim-quantity-input');
        const qty = parseFloat(qtyInput.value) || 0;
        const total = qty * this.pricePerUnit;

        document.getElementById('sim-est-amount').innerText = `₹${total.toFixed(2)}`;
        document.getElementById('meter-rate').innerText = this.pricePerUnit.toFixed(2);
        
        if (!this.isDispensing) {
            document.getElementById('meter-volume').innerText = '0.00';
            document.getElementById('meter-amount').innerText = '0.00';
        }
    },

    startDispensing() {
        const qtyInput = document.getElementById('sim-quantity-input');
        const qty = parseFloat(qtyInput.value);

        if (!this.selectedPump) {
            showToast('Please select a pump dispenser first', 'warning');
            return;
        }

        if (!qty || qty <= 0) {
            showToast('Please enter a valid fuel quantity (> 0)', 'warning');
            qtyInput.focus();
            return;
        }

        if (qty > this.currentStock) {
            showToast(`Requested quantity (${qty}) exceeds available stock (${this.currentStock})`, 'error');
            return;
        }

        const empSelect = document.getElementById('sim-emp-select');
        if (!empSelect.value) {
            showToast('Please select an attendant employee', 'warning');
            return;
        }

        this.targetQuantity = qty;
        this.dispensedVolume = 0;
        this.isDispensing = true;

        // UI State Update
        document.getElementById('sim-status-badge').className = 'badge badge-dispensing';
        document.getElementById('sim-status-badge').innerText = 'DISPENSING';
        document.getElementById('sim-nozzle-status').innerText = 'NOZZLE ACTIVE - FUEL FLOWING';
        document.getElementById('sim-flow-box').classList.add('flow-active');

        document.getElementById('btn-sim-start').disabled = true;
        document.getElementById('btn-sim-stop').disabled = false;
        document.getElementById('btn-sim-complete').disabled = false;
        document.getElementById('btn-sim-reset').disabled = true;

        // Start dispensing animation counter (simulates fuel flowing smoothly)
        const step = Math.max(0.2, this.targetQuantity / 25);
        this.dispenseInterval = setInterval(() => {
            if (this.dispensedVolume < this.targetQuantity) {
                this.dispensedVolume = Math.min(this.targetQuantity, this.dispensedVolume + step);
                const currentAmount = this.dispensedVolume * this.pricePerUnit;

                document.getElementById('meter-volume').innerText = this.dispensedVolume.toFixed(2);
                document.getElementById('meter-amount').innerText = currentAmount.toFixed(2);

                const progressPct = (this.dispensedVolume / this.targetQuantity) * 100;
                document.getElementById('sim-progress-bar').style.width = `${progressPct}%`;

                if (this.dispensedVolume >= this.targetQuantity) {
                    this.stopDispensing(true);
                }
            }
        }, 100);
    },

    stopDispensing(reachedTarget = false) {
        clearInterval(this.dispenseInterval);
        this.isDispensing = false;

        document.getElementById('sim-flow-box').classList.remove('flow-active');
        document.getElementById('sim-status-badge').className = reachedTarget ? 'badge badge-completed' : 'badge badge-pending';
        document.getElementById('sim-status-badge').innerText = reachedTarget ? 'TARGET REACHED' : 'PAUSED';
        document.getElementById('sim-nozzle-status').innerText = reachedTarget ? 'DISPENSING COMPLETE - READY TO FINALIZE' : 'FLOW PAUSED';

        document.getElementById('btn-sim-start').disabled = reachedTarget;
        document.getElementById('btn-sim-stop').disabled = true;
        document.getElementById('btn-sim-complete').disabled = false;
        document.getElementById('btn-sim-reset').disabled = false;
    },

    async completeTransaction() {
        if (this.isDispensing) {
            this.stopDispensing();
        }

        const volumeToCharge = this.dispensedVolume > 0 ? this.dispensedVolume : (parseFloat(document.getElementById('sim-quantity-input').value) || 0);

        if (volumeToCharge <= 0) {
            showToast('No fuel dispensed to bill', 'warning');
            return;
        }

        const payload = {
            pumpCode: this.selectedPump.pumpCode,
            fuelCode: this.selectedFuel ? this.selectedFuel.fuelCode : this.selectedPump.supportedFuelCode,
            quantity: volumeToCharge,
            employeeCode: document.getElementById('sim-emp-select').value,
            customerName: document.getElementById('sim-cust-name').value || 'Walk-in Customer',
            vehicleNumber: document.getElementById('sim-cust-vehicle').value || '',
            paymentMethod: document.getElementById('sim-pay-method').value || 'CASH'
        };

        try {
            const invoice = await API.dispenseFuel(payload);
            showToast(`Transaction ${invoice.transactionNumber} completed successfully!`, 'success');

            // Open Invoice Preview Modal
            BillingModule.renderInvoiceModal(invoice);
            openModal('invoice-modal');

            // Refresh data
            this.resetSimulator();
            this.init(); // Refresh stock
        } catch (err) {
            showToast('Transaction settlement failed: ' + err.message, 'error');
        }
    },

    resetSimulator() {
        clearInterval(this.dispenseInterval);
        this.isDispensing = false;
        this.dispensedVolume = 0;

        document.getElementById('meter-volume').innerText = '0.00';
        document.getElementById('meter-amount').innerText = '0.00';
        document.getElementById('sim-progress-bar').style.width = '0%';
        document.getElementById('sim-flow-box').classList.remove('flow-active');

        document.getElementById('sim-status-badge').className = 'badge badge-online';
        document.getElementById('sim-status-badge').innerText = 'READY';
        document.getElementById('sim-nozzle-status').innerText = 'NOZZLE HOLSTERED - DISPENSER IDLE';

        document.getElementById('btn-sim-start').disabled = false;
        document.getElementById('btn-sim-stop').disabled = true;
        document.getElementById('btn-sim-complete').disabled = true;
        document.getElementById('btn-sim-reset').disabled = false;
    }
};
