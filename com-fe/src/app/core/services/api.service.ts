import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AdminEmployeeRevenue,
  AdminProductRevenue,
  ApiResponse,
  OrderDetailReq,
  SaleHistoryItem,
  SaleOverview
} from '../models/api.model';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly base = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getSaleHistory(employeeId: number): Observable<ApiResponse<SaleHistoryItem[]>> {
    return this.http.get<ApiResponse<SaleHistoryItem[]>>(`${this.base}/reports/sales/${employeeId}/history`);
  }

  getSaleOverview(employeeId: number): Observable<ApiResponse<SaleOverview>> {
    return this.http.get<ApiResponse<SaleOverview>>(`${this.base}/reports/sales/${employeeId}/overview`);
  }

  getAdminProductRevenue(): Observable<ApiResponse<AdminProductRevenue[]>> {
    return this.http.get<ApiResponse<AdminProductRevenue[]>>(`${this.base}/reports/admin/products/revenue`);
  }

  getAdminCompanyRevenue(): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(`${this.base}/reports/admin/company/revenue`);
  }

  getAdminEmployeeRevenue(): Observable<ApiResponse<AdminEmployeeRevenue[]>> {
    return this.http.get<ApiResponse<AdminEmployeeRevenue[]>>(`${this.base}/reports/admin/employees/revenue`);
  }

  createOrderDetail(payload: OrderDetailReq): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.base}/order-details`, payload);
  }
}
