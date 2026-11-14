import { Component, inject, signal } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { ApiService, Customer } from "../core/api.service";
@Component({
  standalone: true,
  imports: [FormsModule],
  template: ` <div
      class="d-flex justify-content-between align-items-center mb-4"
    >
      <div>
        <h1 class="h3 mb-1">Customers</h1>
        <p class="text-secondary mb-0">Customer Service directory</p>
      </div>
      <button class="btn btn-primary" (click)="showForm.set(!showForm())">
        <i class="bi bi-plus-lg me-1"></i>New customer
      </button>
    </div>
    @if (showForm()) {
      <form class="card card-body mb-4" (ngSubmit)="create()">
        <div class="row g-3">
          <div class="col-md-5">
            <label class="form-label">Name</label
            ><input
              class="form-control"
              name="name"
              [(ngModel)]="name"
              required
            />
          </div>
          <div class="col-md-5">
            <label class="form-label">Email</label
            ><input
              class="form-control"
              type="email"
              name="email"
              [(ngModel)]="email"
              required
            />
          </div>
          <div class="col-md-2 d-flex align-items-end">
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
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
            </tr>
          </thead>
          <tbody>
            @for (customer of customers(); track customer.id) {
              <tr>
                <td>{{ customer.id }}</td>
                <td>{{ customer.name }}</td>
                <td>{{ customer.email }}</td>
              </tr>
            } @empty {
              <tr>
                <td colspan="3" class="text-center text-secondary py-4">
                  No customers yet.
                </td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    </div>`,
})
export class CustomersComponent {
  private readonly api = inject(ApiService);
  readonly customers = signal<Customer[]>([]);
  readonly error = signal("");
  readonly showForm = signal(false);
  name = "";
  email = "";
  constructor() {
    this.load();
  }
  load(): void {
    this.api.customers().subscribe({
      next: (rows) => {
        this.customers.set(rows);
        this.error.set("");
      },
      error: () =>
        this.error.set(
          "Customer Service is unavailable. Start the backend services first.",
        ),
    });
  }
  create(): void {
    this.api.createCustomer({ name: this.name, email: this.email }).subscribe({
      next: () => {
        this.name = "";
        this.email = "";
        this.showForm.set(false);
        this.load();
      },
      error: () => this.error.set("Could not save this customer."),
    });
  }
}
