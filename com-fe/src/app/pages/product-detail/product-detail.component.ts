import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../../core/services/api.service';
import { Product } from '../../core/models/api.model';
import { MatDialog } from '@angular/material/dialog';
import { PurchaseConfirmDialogComponent } from '../purchase-confirm-dialog/purchase-confirm-dialog.component';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss']
})
export class ProductDetailComponent implements OnInit {
  product: Product | null = null;
  loading = false;
  affiliateLinkId: number | null = null;

  constructor(
    private route: ActivatedRoute,
    private api: ApiService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    const affCode = this.route.snapshot.queryParamMap.get('aff');

    if (id) {
      this.loadProduct(Number(id));
    }

    const finalAffCode = affCode || sessionStorage.getItem('aff_code');
    if (finalAffCode) {
      this.api.trackAffiliateCode(finalAffCode).subscribe({
        next: (res) => {
          if (res.data) {
            this.affiliateLinkId = res.data.id;
          }
        }
      });
    }
  }

  loadProduct(id: number): void {
    this.loading = true;
    // Mocking getById by fetching the list and filtering
    this.api.getProducts(0, 100).subscribe({
      next: (res) => {
        const list = res?.data?.data || [];
        this.product = list.find(p => p.id === id) || null;
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  buyNow(): void {
    if (!this.product) return;
    
    const dialogRef = this.dialog.open(PurchaseConfirmDialogComponent, {
      width: '450px',
      data: { 
        product: this.product,
        affiliateLinkId: this.affiliateLinkId 
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Handle success (e.g. navigate to orders or show snackbar)
        alert('Đặt hàng thành công!');
      }
    });
  }
}
