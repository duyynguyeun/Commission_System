import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AdminEmployeeRevenue,
  AdminProductRevenue,
  ApiResponse,
  Customer,
  CustomerOrderReq,
  CustomerReq,
  Employee,
  EmployeeReq,
  OrderDetailReq,
  PageResponse,
  Product,
  ProductReq,
  SaleHistoryItem,
  SaleOverview,
  SaleProduct,
  AffiliateLinkRenderReq,
  AffiliateLinkRes,
  CommissionPolicy,
  CommissionPolicyReq
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

  getProducts(page: number = 0, size: number = 20): Observable<ApiResponse<PageResponse<Product>>> {
    return this.http.get<ApiResponse<PageResponse<Product>>>(`${this.base}/products?page=${page}&size=${size}`);
  }

  getSaleProducts(page: number = 0, size: number = 20): Observable<ApiResponse<PageResponse<SaleProduct>>> {
    return this.http.get<ApiResponse<PageResponse<SaleProduct>>>(`${this.base}/products/sale?page=${page}&size=${size}`);
  }

  createProduct(payload: ProductReq): Observable<ApiResponse<Product>> {
    return this.http.post<ApiResponse<Product>>(`${this.base}/products`, payload);
  }

  updateProduct(id: number, payload: ProductReq): Observable<ApiResponse<Product>> {
    return this.http.post<ApiResponse<Product>>(`${this.base}/products/${id}`, payload);
  }

  deleteProduct(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.base}/products/${id}`);
  }

  getEmployees(page: number = 0, size: number = 20): Observable<ApiResponse<PageResponse<Employee>>> {
    return this.http.get<ApiResponse<PageResponse<Employee>>>(`${this.base}/employees?page=${page}&size=${size}`);
  }

  createEmployee(payload: EmployeeReq): Observable<ApiResponse<Employee>> {
    return this.http.post<ApiResponse<Employee>>(`${this.base}/employees`, payload);
  }

  updateEmployee(id: number, payload: EmployeeReq): Observable<ApiResponse<Employee>> {
    return this.http.post<ApiResponse<Employee>>(`${this.base}/employees/${id}`, payload);
  }

  deleteEmployee(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.base}/employees/${id}`);
  }

  getEmployeesByRole(role: string): Observable<ApiResponse<Employee[]>> {
    return this.http.get<ApiResponse<Employee[]>>(`${this.base}/employees/role/${role}`);
  }

  getCustomers(page: number = 0, size: number = 20): Observable<ApiResponse<PageResponse<Customer>>> {
    return this.http.get<ApiResponse<PageResponse<Customer>>>(`${this.base}/customers?page=${page}&size=${size}`);
  }

  createCustomer(payload: CustomerReq): Observable<ApiResponse<Customer>> {
    return this.http.post<ApiResponse<Customer>>(`${this.base}/customers`, payload);
  }

  updateCustomer(id: number, payload: CustomerReq): Observable<ApiResponse<Customer>> {
    return this.http.post<ApiResponse<Customer>>(`${this.base}/customers/${id}`, payload);
  }

  deleteCustomer(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(`${this.base}/customers/${id}`);
  }

  createCustomerOrder(payload: CustomerOrderReq): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(`${this.base}/orders`, payload);
  }

  renderAffiliateLink(payload: AffiliateLinkRenderReq): Observable<ApiResponse<AffiliateLinkRes>> {
    return this.http.post<ApiResponse<AffiliateLinkRes>>(`${this.base}/affiliate-links/render`, payload);
  }

  trackAffiliateCode(affCode: string): Observable<ApiResponse<AffiliateLinkRes>> {
    return this.http.get<ApiResponse<AffiliateLinkRes>>(`${this.base}/affiliate-links/track/${affCode}`);
  }

  getCommissionPolicies(page: number = 0, size: number = 1000): Observable<ApiResponse<PageResponse<CommissionPolicy>>> {
    return this.http.get<ApiResponse<PageResponse<CommissionPolicy>>>(`${this.base}/policy?page=${page}&size=${size}`);
  }

  updateCommissionPolicy(id: number, payload: CommissionPolicyReq): Observable<ApiResponse<CommissionPolicy>> {
    return this.http.post<ApiResponse<CommissionPolicy>>(`${this.base}/policy/${id}`, payload);
  }

  createCommissionPolicy(payload: CommissionPolicyReq): Observable<ApiResponse<CommissionPolicy>> {
    return this.http.post<ApiResponse<CommissionPolicy>>(`${this.base}/policy`, payload);
  }
}
