import { CommonModule } from "@angular/common";
import { Component, inject, signal } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { ApiService, BankAccount, Customer } from "../core/api.service";
@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: ` <div
      class="d-flex justify-content-between align-items-center mb-4"
    >
      <div>
        <h1 class="h3 mb-1">Bank accounts</h1>
        <p class="text-secondary mb-0">Accounts linked to customers</p>
      </div>
      <button class="btn btn-primary" (click)="showForm.set(!showForm())">
        <i class="bi bi-plus-lg me-1"></i>New account
      </button>
    </div>
    @if (showForm()) {
      <form class="card card-body mb-4" (ngSubmit)="create()">
        <div class="row g-3">
          <div class="col-md-4">
            <label class="form-label">Balance</label
            ><input
              class="form-control"
              type="number"
              min="0"
              step="0.01"
              name="balance"
              [(ngModel)]="balance"
              required
            />
          </div>
          <div class="col-md-4">
            <label class="form-label">Type</label
            ><select class="form-select" name="type" [(ngModel)]="type">
              <option>current account</option>
              <option>savings account</option>
            </select>
          </div>
          <div class="col-md-3">
            <label class="form-label">Customer</label
            ><select
              class="form-select"
              name="customerId"
              [(ngModel)]="customerId"
              required
            >
              <option [ngValue]="0" disabled>Select customer</option>
              @for (customer of customers(); track customer.id) {
                <option [ngValue]="customer.id">
                  {{ customer.name }} (#{{ customer.id }})
                </option>
              }
            </select>
          </div>
          <div class="col-md-1 d-flex align-items-end">
            <button class="btn btn-success w-100">Save</button>
          </div>
        </div>
      </form>
    }
    @if (error()) {
      <div class="alert alert-danger">{{ error() }}</div>
    }
    <div class="card">
      <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
          <thead>
            <tr>
              <th>Account ID</th>
              <th>Customer</th>
              <th>Type</th>
              <th>Balance</th>
              <th>Created</th>
            </tr>
          </thead>
          <tbody>
            @for (account of accounts(); track account.id) {
              <tr>
                <td>
                  <code>{{ account.id }}</code>
                </td>
                <td>
                  {{ account.customer?.name ?? "not available" }}
                  <span class="text-secondary"
                    >(#{{ account.customerId }})</span
                  >
                </td>
                <td>{{ account.type }}</td>
                <td>{{ account.balance | number: "1.2-2" }}</td>
                <td>{{ account.createdAt | date: "medium" }}</td>
              </tr>
            } @empty {
              <tr>
                <td colspan="5" class="text-center text-secondary py-4">
                  No accounts yet.
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    </div>`,
})
export class AccountsComponent {
  private readonly api = inject(ApiService);
  readonly accounts = signal<BankAccount[]>([]);
  readonly customers = signal<Customer[]>([]);
  readonly error = signal("");
  readonly showForm = signal(false);
  balance = 0;
  type = "current account";
  customerId = 0;
  constructor() {
    this.load();
    this.api
      .customers()
      .subscribe({ next: (rows) => this.customers.set(rows) });
  }
  load(): void {
    this.api.accounts().subscribe({
      next: (rows) => {
        this.accounts.set(rows);
        this.error.set("");
      },
      error: () =>
        this.error.set(
          "EBank Service is unavailable. Start the backend services first.",
        ),
    });
  }
  create(): void {
    this.api
      .createAccount({
        balance: this.balance,
        type: this.type,
        customerId: this.customerId,
      })
      .subscribe({
        next: () => {
          this.showForm.set(false);
          this.load();
        },
        error: () =>
          this.error.set(
            "Could not save this account. Check that the selected customer exists.",
          ),
      });
  }
}
