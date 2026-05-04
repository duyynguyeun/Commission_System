import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { RegisterReq } from '../../core/models/api.model';

@Component({
  selector: 'app-register-dialog',
  templateUrl: './register-dialog.component.html',
  styleUrls: ['./register-dialog.component.scss']
})
export class RegisterDialogComponent implements OnInit {
  registerForm!: FormGroup;
  error = '';

  constructor(
    private dialogRef: MatDialogRef<RegisterDialogComponent>,
    private auth: AuthService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      fullName: ['', [Validators.required]],
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: ['SALE_PARENT', [Validators.required]],
      parentId: [null]
    });

    this.registerForm.get('role')?.valueChanges.subscribe((role) => {
      const parentIdControl = this.registerForm.get('parentId');

      if (role === 'SALE_CHILD') {
        parentIdControl?.setValidators([Validators.required, Validators.min(1)]);
      } else {
        parentIdControl?.clearValidators();
      }

      parentIdControl?.updateValueAndValidity();
    });
  }

  submit(): void {
    this.error = '';

    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      this.error = 'Vui lòng nhập đầy đủ thông tin hợp lệ';
      return;
    }

    const formValue = this.registerForm.value;
    const payload: RegisterReq = {
      fullName: formValue.fullName,
      username: formValue.username,
      password: formValue.password,
      role: formValue.role,
      parentId: formValue.role === 'SALE_CHILD' ? Number(formValue.parentId) : null
    };

    this.auth.register(payload).subscribe({
      next: (res) => {
        const user = res.data;
        this.auth.saveUser(user);
        this.dialogRef.close();
        if (user.role === 'ADMIN') {
          this.router.navigate(['/app/admin/company']);
          return;
        }
        this.router.navigate(['/app/sale/overview']);
      },
      error: () => this.error = 'Đăng ký thất bại'
    });
  }

  close(): void {
    this.dialogRef.close();
  }

  get fullNameControl() {
    return this.registerForm.get('fullName');
  }

  get usernameControl() {
    return this.registerForm.get('username');
  }

  get passwordControl() {
    return this.registerForm.get('password');
  }

  get roleControl() {
    return this.registerForm.get('role');
  }

  get parentIdControl() {
    return this.registerForm.get('parentId');
  }
}
