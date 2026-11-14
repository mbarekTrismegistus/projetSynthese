import { Component } from "@angular/core";
import { RouterLink, RouterLinkActive, RouterOutlet } from "@angular/router";
@Component({
  selector: "app-root",
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: ` <nav class="navbar navbar-expand navbar-dark bg-primary">
      <div class="container">
        <a class="navbar-brand fw-bold" routerLink="/accounts"
          ><i class="bi bi-bank me-2"></i>eBank</a
        >
        <div class="navbar-nav ms-auto">
          <a class="nav-link" routerLink="/customers" routerLinkActive="active"
            >Customers</a
          ><a class="nav-link" routerLink="/accounts" routerLinkActive="active"
            >Accounts</a
          ><a class="nav-link" routerLink="/chat" routerLinkActive="active"
            >AI Assistant</a
          >
        </div>
      </div>
    </nav>
    <main class="container py-4 app-shell"><router-outlet /></main>`,
})
export class AppComponent {}
