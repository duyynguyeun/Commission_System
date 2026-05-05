import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-affiliate-link-dialog',
  template: `
    <div class="aff-dialog">
      <div class="dialog-header">
        <h3>Link Tiếp Thị Của Bạn</h3>
        <button (click)="close()" class="btn-close"><i class='bx bx-x'></i></button>
      </div>
      <div class="dialog-body">
        <p>Sử dụng link này để giới thiệu sản phẩm và nhận hoa hồng:</p>
        <div class="link-box">
          <input #linkInput readonly [value]="fullUrl" />
          <button (click)="copy(linkInput)" class="btn-copy">
            <i class='bx bx-copy'></i> {{ copied ? 'Đã chép' : 'Sao chép' }}
          </button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .aff-dialog { padding: 20px; min-width: 450px; font-family: 'Inter', sans-serif; }
    .dialog-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; border-bottom: 1px solid #f1f2f6; padding-bottom: 12px; }
    .dialog-header h3 { margin: 0; color: #2d3436; font-size: 1.25rem; font-weight: 700; }
    .btn-close { background: transparent; border: none; font-size: 24px; color: #b2bec3; cursor: pointer; transition: color 0.2s; }
    .btn-close:hover { color: #ff7675; }
    .dialog-body p { color: #636e72; font-size: 14px; margin-bottom: 15px; line-height: 1.5; }
    .link-box { display: flex; gap: 8px; background: #f8f9fa; padding: 12px; border-radius: 12px; border: 1px solid #dfe6e9; box-shadow: inset 0 2px 4px rgba(0,0,0,0.02); }
    .link-box input { flex: 1; border: none; background: transparent; color: #4f46e5; font-weight: 600; font-size: 14px; outline: none; }
    .btn-copy { background: #4f46e5; color: white; border: none; padding: 10px 18px; border-radius: 10px; font-size: 13px; font-weight: 600; cursor: pointer; display: flex; align-items: center; gap: 8px; transition: all 0.2s; white-space: nowrap; }
    .btn-copy:hover { background: #4338ca; transform: translateY(-1px); box-shadow: 0 4px 12px rgba(79, 70, 229, 0.25); }
    .btn-copy:active { transform: translateY(0); }
  `]
})
export class AffiliateLinkDialogComponent {
  copied = false;
  fullUrl = '';

  constructor(
    public dialogRef: MatDialogRef<AffiliateLinkDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { affUrl: string }
  ) {
    this.fullUrl = window.location.origin + data.affUrl;
  }

  close() { this.dialogRef.close(); }

  copy(input: HTMLInputElement) {
    input.select();
    document.execCommand('copy');
    this.copied = true;
    setTimeout(() => this.copied = false, 2000);
  }
}
