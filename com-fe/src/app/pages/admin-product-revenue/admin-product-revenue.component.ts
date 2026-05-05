import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { AdminProductRevenue } from '../../core/models/api.model';
import { ColDef, GridApi, GridReadyEvent } from 'ag-grid-community';

@Component({
  selector: 'app-admin-product-revenue',
  templateUrl: './admin-product-revenue.component.html',
  styleUrls: ['./admin-product-revenue.component.scss']
})
export class AdminProductRevenueComponent implements OnInit {
  data: AdminProductRevenue[] = [];
  
  columnDefs: ColDef[] = [
    { 
      field: 'productId', 
      headerName: 'ID', 
      width: 80,
      cellStyle: { fontWeight: 'bold', color: '#666' }
    },
    { 
      field: 'productName', 
      headerName: 'Sản phẩm', 
      flex: 1,
      minWidth: 200,
      cellRenderer: (params: any) => `
        <div class="product-cell">
          <div class="product-icon"><i class='bx bx-package'></i></div>
          <span>${params.value}</span>
        </div>
      `
    },
    { 
      field: 'price', 
      headerName: 'Giá bán', 
      width: 140,
      valueFormatter: (params: any) => params.value.toLocaleString() + ' đ'
    },
    { 
      field: 'quantitySold', 
      headerName: 'Đã bán', 
      width: 120,
      cellRenderer: (params: any) => `
        <span class="badge ${params.value > 0 ? 'badge-success' : 'badge-neutral'}">
          ${params.value} sp
        </span>
      `
    },
    { 
      field: 'revenue', 
      headerName: 'Doanh thu', 
      width: 160,
      cellClass: 'revenue-cell',
      valueFormatter: (params: any) => params.value.toLocaleString() + ' đ'
    }
  ];

  defaultColDef: ColDef = {
    sortable: true,
    filter: true,
    resizable: true
  };

  gridApi!: GridApi;
  currentPage = 1;
  pageSize = 10;
  totalPages = 1;
  totalRecords = 0;
  Math = Math;

  constructor(private api: ApiService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.api.getAdminProductRevenue().subscribe(res => {
      this.data = res.data || [];
      this.totalRecords = this.data.length;
      this.totalPages = Math.ceil(this.totalRecords / this.pageSize) || 1;
      this.currentPage = 1;
      if (this.gridApi) this.gridApi.paginationGoToPage(0);
    });
  }

  onGridReady(params: GridReadyEvent): void {
    this.gridApi = params.api;
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    if (this.gridApi) this.gridApi.paginationGoToPage(page - 1);
  }

  getPages(): (number | string)[] {
    const t = this.totalPages, c = this.currentPage;
    if (t <= 7) return Array.from({ length: t }, (_, i) => i + 1);
    const pages: (number | string)[] = [1];
    if (c > 3) pages.push('...');
    for (let i = Math.max(2, c - 1); i <= Math.min(t - 1, c + 1); i++) pages.push(i);
    if (c < t - 2) pages.push('...');
    pages.push(t);
    return pages;
  }

  get totalRevenue(): number {
    return this.data.reduce((sum, item) => sum + item.revenue, 0);
  }

  get totalItemsSold(): number {
    return this.data.reduce((sum, item) => sum + item.quantitySold, 0);
  }
}
