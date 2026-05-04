import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { AdminEmployeeRevenue } from '../../core/models/api.model';

@Component({
  selector: 'app-admin-employee-revenue',
  templateUrl: './admin-employee-revenue.component.html'
})
export class AdminEmployeeRevenueComponent implements OnInit {
  data: AdminEmployeeRevenue[] = [];

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.api.getAdminEmployeeRevenue().subscribe(res => this.data = res.data || []);
  }
}
