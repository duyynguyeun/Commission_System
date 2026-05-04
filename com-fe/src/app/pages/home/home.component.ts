import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { AuthRes, RegisterReq, UserRole } from '../../core/models/api.model';
import { MatDialog } from '@angular/material/dialog';
import { LoginComponent } from '../login/login.component';
import { RegisterDialogComponent } from '../register-dialog/register-dialog.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  private static readonly LOGIN_DIALOG_ID = 'login-dialog';
  private static readonly REGISTER_DIALOG_ID = 'register-dialog';

  mode: 'login' | 'register' = 'login';
  message = '';

  loginModel = { username: '', password: '' };
  registerModel: RegisterReq = {
    username: '',
    password: '',
    fullName: '',
    role: 'CUSTOMER',
    parentId: null
  };

  roles: UserRole[] = ['CUSTOMER', 'SALE_CHILD', 'SALE_PARENT', 'ADMIN'];

  constructor(public auth: AuthService, private router: Router, private dialog: MatDialog) {}

  switchMode(mode: 'login' | 'register'): void {
    this.mode = mode;
    this.message = '';
  }

  login(): void {
    this.message = '';
    this.auth.login(this.loginModel).subscribe({
      next: (res) => {
        const user = res.data;
        this.auth.saveAuth(user);
        this.message = `Đăng nhập thành công: ${user.role}`;
        this.navigateByRole(user);
      },
      error: (err) => this.message = this.resolveError(err, 'Đăng nhập thất bại')
    });
  }

  register(): void {
    this.message = '';
    this.auth.register(this.registerModel).subscribe({
      next: (res) => {
        const user = res.data;
        this.auth.saveAuth(user);
        this.message = 'Đăng ký thành công và đã đăng nhập';
        this.navigateByRole(user);
      },
      error: (err) => this.message = this.resolveError(err, 'Đăng ký thất bại')
    });
  }

  logout(): void {
    this.auth.logout();
    this.message = 'Đã đăng xuất';
  }

  openLogin(): void {
    // Prevent duplicate dialogs (same pattern as SchoolSubjectComponent)
    const existing = this.dialog.getDialogById(HomeComponent.LOGIN_DIALOG_ID);
    if (existing) existing.close();

    const dialogRef = this.dialog.open(LoginComponent, {
      id: HomeComponent.LOGIN_DIALOG_ID,
      width: '440px',
      height: 'auto',
      autoFocus: false,
      data: {}
    });

    dialogRef.afterClosed().subscribe(() => {});
  }

  openRegister(): void {
    // Prevent duplicate dialogs (same pattern as SchoolSubjectComponent)
    const existing = this.dialog.getDialogById(HomeComponent.REGISTER_DIALOG_ID);
    if (existing) existing.close();

    const dialogRef = this.dialog.open(RegisterDialogComponent, {
      id: HomeComponent.REGISTER_DIALOG_ID,
      width: '560px',
      height: 'auto',
      autoFocus: false,
      data: {}
    });

    dialogRef.afterClosed().subscribe(() => {});
  }

  private resolveError(err: any, fallback: string): string {
    if (err?.status === 0) {
      return 'Không kết nối được backend (localhost:8080). Hãy chạy backend trước.';
    }
    return err?.error?.message || fallback;
  }

  private navigateByRole(user: AuthRes): void {
    if (user.role === 'ADMIN') {
      this.router.navigate(['/app/admin/company']);
      return;
    }
    if (user.role === 'SALE_PARENT' || user.role === 'SALE_CHILD') {
      this.router.navigate(['/app/sale/overview']);
      return;
    }
    this.router.navigate(['/']);
  }
}
