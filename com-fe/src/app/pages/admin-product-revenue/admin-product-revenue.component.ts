import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { AdminProductRevenue } from '../../core/models/api.model';

@Component({
  selector: 'app-admin-product-revenue',
  templateUrl: './admin-product-revenue.component.html'
})
export class AdminProductRevenueComponent implements OnInit {
  data: AdminProductRevenue[] = [];

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.api.getAdminProductRevenue().subscribe(res => this.data = res.data || []);
  }
}
