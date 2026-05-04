import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { AuthRes } from '../../core/models/api.model';


@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent {
  user: AuthRes | null;

  constructor(private auth: AuthService, private router: Router) {
    this.user = this.auth.getUser();
  }

  get userInitial(): string {
    return this.user?.username ? this.user.username.charAt(0).toUpperCase() : 'U';
  }

  openLogin(): void {
    // no-op: moved to topbar in HomeComponent
  }

  openRegister(): void {
    // no-op: moved to topbar in HomeComponent
  }

  isAdmin(): boolean {
    return this.user?.role === 'ADMIN';
  }

  isSale(): boolean {
    return this.user?.role === 'SALE_PARENT' || this.user?.role === 'SALE_CHILD';
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
