import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { AdminCompanyRevenue } from '../../core/models/api.model';

@Component({
  selector: 'app-admin-company-revenue',
  templateUrl: './admin-company-revenue.component.html',
  styleUrls: ['./admin-company-revenue.component.scss']
})
export class AdminCompanyRevenueComponent implements OnInit {
  revenue: AdminCompanyRevenue = {
    totalSalesRevenue: 0,
    directSalesRevenue: 0,
    affiliateSalesRevenue: 0,
    totalCommissionPaid: 0,
    netCompanyRevenue: 0
  };
  loading = true;

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.loadRevenue();
  }

  loadRevenue(): void {
    this.loading = true;
    this.api.getAdminCompanyRevenue().subscribe({
      next: res => {
        this.revenue = res.data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }
}
