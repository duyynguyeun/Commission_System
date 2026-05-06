import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { SaleOverview } from '../../core/models/api.model';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-sale-overview',
  templateUrl: './sale-overview.component.html',
  styleUrls: ['./sale-overview.component.scss']
})
export class SaleOverviewComponent implements OnInit {
  employeeId: number = 0;
  userRole: string = '';
  data?: SaleOverview;
  loading: boolean = false;

  constructor(private api: ApiService, private auth: AuthService) {}

  ngOnInit(): void {
    const user = this.auth.getUser();
    if (user) {
      this.employeeId = user.userId;
      this.userRole = user.role;
      this.load();
    }
  }

  load(): void {
    if (this.employeeId > 0) {
      this.loading = true;
      this.api.getSaleOverview(this.employeeId).subscribe({
        next: (res) => {
          this.data = res.data;
          this.loading = false;
        },
        error: () => {
          this.loading = false;
        }
      });
    }
  }
}
