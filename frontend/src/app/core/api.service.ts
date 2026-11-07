import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
export interface Customer { id: number; name: string; email: string; }
export interface BankAccount { id: string; createdAt: string; balance: number; type: string; customerId: number; customer?: Customer; }
@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly http = inject(HttpClient);
  private readonly base = 'http://localhost:9999';
  customers(): Observable<Customer[]> { return this.http.get<Customer[]>(`${this.base}/customers`); }
  createCustomer(data: Pick<Customer, 'name' | 'email'>): Observable<Customer> { return this.http.post<Customer>(`${this.base}/customers`, data); }
  accounts(): Observable<BankAccount[]> { return this.http.get<BankAccount[]>(`${this.base}/accounts`); }
  createAccount(data: Pick<BankAccount, 'balance' | 'type' | 'customerId'>): Observable<BankAccount> { return this.http.post<BankAccount>(`${this.base}/accounts`, data); }
  chat(query: string): Observable<string> { return this.http.post(`${this.base}/chat`, query, { responseType: 'text' }); }
}
