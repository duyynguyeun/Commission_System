import { Component, Optional } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';

  constructor(private auth: AuthService, private router: Router, @Optional() private dialogRef?: MatDialogRef<LoginComponent>) {}

  submit(): void {
    this.error = '';
    this.auth.login({ username: this.username, password: this.password }).subscribe({
      next: (res) => {
        const user = res.data;
        this.auth.saveUser(user);
        if (this.dialogRef) {
          this.dialogRef.close();
        }
        if (user.role === 'ADMIN') {
          this.router.navigate(['/app/admin/company']);
          return;
        }
        this.router.navigate(['/app/sale/overview']);
      },
      error: () => this.error = 'Đăng nhập thất bại'
    });
  }

  close(): void {
    if (this.dialogRef) {
      this.dialogRef.close();
    }
  }
}
