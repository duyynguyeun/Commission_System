import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../core/services/api.service';

@Component({
  selector: 'app-admin-company-revenue',
  templateUrl: './admin-company-revenue.component.html'
})
export class AdminCompanyRevenueComponent implements OnInit {
  revenue = 0;

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.api.getAdminCompanyRevenue().subscribe(res => this.revenue = res.data || 0);
  }
}
