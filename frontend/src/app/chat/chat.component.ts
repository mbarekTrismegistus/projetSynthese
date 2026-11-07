import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../core/api.service';
interface Message { role: 'user' | 'assistant'; text: string; }
@Component({ standalone: true, imports: [FormsModule], template: `
<div class="mb-4"><h1 class="h3 mb-1">eBank AI Assistant</h1><p class="text-secondary mb-0">Ask about customers and bank accounts.</p></div>
<div class="card"><div class="chat-window p-3 d-flex flex-column gap-3" #history>@for (message of messages(); track $index) { <div class="d-flex" [class.justify-content-end]="message.role === 'user'"><div class="chat-message rounded-3 px-3 py-2" [class.bg-primary]="message.role === 'user'" [class.text-white]="message.role === 'user'" [class.bg-light]="message.role === 'assistant'"><small class="d-block opacity-75">{{ message.role === 'user' ? 'You' : 'eBank Assistant' }}</small>{{ message.text }}</div></div> } @if (busy()) { <div class="text-secondary"><span class="spinner-border spinner-border-sm me-2"></span>Thinking…</div> }</div>
<form class="border-top p-3" (ngSubmit)="send()"><div class="input-group"><input class="form-control" name="query" [(ngModel)]="query" placeholder="Example: Show accounts for customer 1" [disabled]="busy()"><button class="btn btn-primary" [disabled]="busy() || !query.trim()"><i class="bi bi-send me-1"></i>Send</button></div></form></div>` })
export class ChatComponent {
  private readonly api = inject(ApiService);
  readonly messages = signal<Message[]>([{ role: 'assistant', text: 'Bonjour ! Je peux rechercher des clients et des comptes bancaires.' }]);
  readonly busy = signal(false); query = '';
  send(): void {
    const question = this.query.trim(); if (!question || this.busy()) return;
    this.messages.update(rows => [...rows, { role: 'user', text: question }]); this.query = ''; this.busy.set(true);
    this.api.chat(question).subscribe({ next: answer => { this.messages.update(rows => [...rows, { role: 'assistant', text: answer }]); this.busy.set(false); }, error: () => { this.messages.update(rows => [...rows, { role: 'assistant', text: 'The chatbot is unavailable. Check the OpenAI key and MCP services.' }]); this.busy.set(false); } });
  }
}
