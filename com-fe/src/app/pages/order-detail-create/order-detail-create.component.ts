import { Component } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { OrderDetailReq } from '../../core/models/api.model';

@Component({
  selector: 'app-order-detail-create',
  templateUrl: './order-detail-create.component.html'
})
export class OrderDetailCreateComponent {
  model: OrderDetailReq = {
    price: 0,
    quantity: 1,
    customerOrderId: 0,
    productId: 0,
    sellerId: 0,
    parentId: undefined,
    affiliateLinkId: 0
  };
  message = '';

  constructor(private api: ApiService) {}

  submit(): void {
    this.api.createOrderDetail(this.model).subscribe({
      next: () => this.message = 'Tao order detail thanh cong, commission da duoc tao tu dong.',
      error: () => this.message = 'Tao order detail that bai. Kiem tra lai id va policy.'
    });
  }
}
