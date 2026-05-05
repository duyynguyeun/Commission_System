import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ApiService } from '../../core/services/api.service';
import { AuthService } from '../../core/services/auth.service';
import { Product, CustomerOrderReq } from '../../core/models/api.model';

@Component({
  selector: 'app-purchase-confirm-dialog',
  templateUrl: './purchase-confirm-dialog.component.html',
  styleUrls: ['./purchase-confirm-dialog.component.scss']
})
export class PurchaseConfirmDialogComponent implements OnInit {
  quantity = 1;
  loading = false;
  error = '';

  constructor(
    private dialogRef: MatDialogRef<PurchaseConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { product: Product },
    private api: ApiService,
    private auth: AuthService
  ) {}

  ngOnInit(): void {}

  get totalPrice(): number {
    return this.quantity * this.data.product.price;
  }

  increment(): void {
    if (this.quantity < this.data.product.stockQuantity) {
      this.quantity++;
    }
  }

  decrement(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  submit(): void {
    if (this.quantity < 1 || this.quantity > this.data.product.stockQuantity) {
      this.error = 'Số lượng không hợp lệ';
      return;
    }

    const user = this.auth.getUser();
    if (!user) {
      this.error = 'Bạn cần đăng nhập để mua hàng';
      return;
    }

    this.loading = true;
    this.error = '';

    // Step 1: Create Customer Order
    const orderPayload: CustomerOrderReq = {
      totalPrice: this.totalPrice,
      status: 'PENDING',
      customerId: user.userId || user.accountId // Fallback in case property differs
    };

    this.api.createCustomerOrder(orderPayload).subscribe({
      next: (orderRes) => {
        // Step 2: Create Order Detail linking to this order
        const customerOrderId = orderRes.data.id;
        this.api.createOrderDetail({
          price: this.data.product.price,
          quantity: this.quantity,
          customerOrderId: customerOrderId,
          productId: this.data.product.id,
          sellerId: null, // Direct purchase
          affiliateLinkId: null // No affiliate link
        }).subscribe({
          next: () => {
            this.loading = false;
            this.dialogRef.close(true);
          },
          error: () => {
            this.loading = false;
            this.error = 'Lỗi khi tạo chi tiết đơn hàng';
          }
        });
      },
      error: () => {
        this.loading = false;
        this.error = 'Lỗi khi tạo đơn hàng. Vui lòng thử lại.';
      }
    });
  }

  close(): void {
    this.dialogRef.close(false);
  }
}
