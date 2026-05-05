import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { AdminEmployeeRevenue } from '../../core/models/api.model';
import { ColDef, GridApi, GridReadyEvent } from 'ag-grid-community';

@Component({
  selector: 'app-admin-employee-revenue',
  templateUrl: './admin-employee-revenue.component.html',
  styleUrls: ['./admin-employee-revenue.component.scss']
})
export class AdminEmployeeRevenueComponent implements OnInit {
  data: AdminEmployeeRevenue[] = [];
  
  columnDefs: ColDef[] = [
    { 
      headerName: 'STT',
      valueGetter: 'node.rowIndex + 1',
      width: 70,
      cellStyle: { fontWeight: 'bold', color: '#666', textAlign: 'center' }
    },
    { 
      field: 'employeeName', 
      headerName: 'Nhân viên', 
      width: 150,
      cellRenderer: (params: any) => `
        <div class="user-cell">
          <div class="user-avatar"><i class='bx bx-user'></i></div>
          <span>${params.value}</span>
        </div>
      `
    },
    { 
      field: 'role', 
      headerName: 'Vai trò', 
      width: 130,
      cellRenderer: (params: any) => {
        let cls = 'badge-neutral';
        if (params.value === 'ADMIN') cls = 'badge-error';
        if (params.value === 'SALE_PARENT') cls = 'badge-blue';
        if (params.value === 'SALE_CHILD') cls = 'badge-success';
        return `<span class="badge ${cls}">${params.value}</span>`;
      }
    },
    { 
      field: 'salesRevenue', 
      headerName: 'Doanh thu bán', 
      width: 140,
      cellClass: 'revenue-cell',
      valueFormatter: (params: any) => (params.value || 0).toLocaleString() + ' đ'
    },
    { 
      field: 'totalCommission', 
      headerName: 'Hoa hồng nhận', 
      width: 140,
      cellClass: 'commission-cell',
      valueFormatter: (params: any) => (params.value || 0).toLocaleString() + ' đ'
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
    this.api.getAdminEmployeeRevenue().subscribe(res => {
      this.data = res.data || [];
      this.totalRecords = this.data.length;
      this.totalPages = Math.ceil(this.totalRecords / this.pageSize) || 1;
      this.currentPage = 1;
      if (this.gridApi) this.gridApi.paginationGoToPage(0);
    });
  }

  onGridReady(params: GridReadyEvent): void {
    this.gridApi = params.api;
    setTimeout(() => params.api.sizeColumnsToFit(), 100);
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

  get totalSales(): number {
    return this.data.reduce((sum, item) => sum + (item.salesRevenue || 0), 0);
  }

  get totalCommission(): number {
    return this.data.reduce((sum, item) => sum + (item.totalCommission || 0), 0);
  }
}
