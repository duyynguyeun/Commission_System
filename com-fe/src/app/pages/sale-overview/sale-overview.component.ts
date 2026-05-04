import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { SaleOverview } from '../../core/models/api.model';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-sale-overview',
  templateUrl: './sale-overview.component.html'
})
export class SaleOverviewComponent implements OnInit {
  employeeId = 0;
  data?: SaleOverview;

  constructor(private api: ApiService, private auth: AuthService) {}

  ngOnInit(): void {
    const user = this.auth.getUser();
    this.employeeId = user?.userId || 0;
    this.load();
  }

  load(): void {
    if (!this.employeeId) { return; }
    this.api.getSaleOverview(this.employeeId).subscribe(res => this.data = res.data);
  }
}
