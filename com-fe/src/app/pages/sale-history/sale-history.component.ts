import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { SaleHistoryItem } from '../../core/models/api.model';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-sale-history',
  templateUrl: './sale-history.component.html'
})
export class SaleHistoryComponent implements OnInit {
  employeeId = 0;
  data: SaleHistoryItem[] = [];

  constructor(private api: ApiService, private auth: AuthService) {}

  ngOnInit(): void {
    const user = this.auth.getUser();
    this.employeeId = user?.userId || 0;
    this.load();
  }

  load(): void {
    if (!this.employeeId) { return; }
    this.api.getSaleHistory(this.employeeId).subscribe(res => this.data = res.data || []);
  }
}
