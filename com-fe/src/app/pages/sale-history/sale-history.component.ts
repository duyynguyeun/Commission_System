import { Component, OnInit } from '@angular/core';
import { ColDef, GridApi, GridReadyEvent } from 'ag-grid-community';
import { ApiService } from '../../core/services/api.service';
import { SaleHistoryItem } from '../../core/models/api.model';
import { AuthService } from '../../core/services/auth.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-sale-history',
  templateUrl: './sale-history.component.html',
  styleUrls: ['./sale-history.component.scss'],
  providers: [DatePipe]
})
export class SaleHistoryComponent implements OnInit {
  employeeId = 0;
  historyData: SaleHistoryItem[] = [];
  filteredData: SaleHistoryItem[] = [];
  loading = false;
  searchTerm = '';
  gridApi!: GridApi;
  gridHeight = '520px';

  currentPage = 1;
  pageSize = 10;
  totalPages = 1;
  totalRecords = 0;
  Math = Math;

  columnDefs: ColDef[] = [
    {
      headerName: 'STT', width: 70, sortable: false, filter: false,
      valueGetter: 'node.rowIndex + 1',
      cellClass: 'cell-center', headerClass: 'header-center'
    },
    { field: 'productName', headerName: 'Tên sản phẩm', flex: 1.5, minWidth: 200, cellClass: 'cell-name' },
    { field: 'quantity', headerName: 'Số lượng', width: 100, cellClass: 'cell-center', headerClass: 'header-center' },
    {
      field: 'lineRevenue', headerName: 'Tổng tiền', width: 150, cellClass: 'cell-price',
      valueFormatter: params => this.formatCurrency(params.value)
    },
    {
      field: 'commissionAmount', headerName: 'Hoa hồng nhận', width: 150, cellClass: 'cell-commission',
      valueFormatter: params => this.formatCurrency(params.value)
    },
    { field: 'sellerName', headerName: 'Người giới thiệu', width: 180, cellClass: 'cell-seller' },
    {
      field: 'transactionAt', headerName: 'Thời gian', width: 180,
      valueFormatter: params => this.formatDate(params.value)
    }
  ];

  constructor(
    private api: ApiService, 
    private auth: AuthService,
    private datePipe: DatePipe
  ) {}

  ngOnInit(): void {
    const user = this.auth.getUser();
    this.employeeId = user?.userId || 0;
    this.loadHistory();
  }

  onGridReady(params: GridReadyEvent): void {
    this.gridApi = params.api;
    this.gridApi.paginationSetPageSize(this.pageSize);
  }

  loadHistory(): void {
    if (!this.employeeId) return;
    this.loading = true;
    this.api.getSaleHistory(this.employeeId).subscribe({
      next: res => {
        this.historyData = res.data || [];
        this.applyFilter();
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  applyFilter(): void {
    const keyword = this.searchTerm.trim().toLowerCase();
    this.filteredData = this.historyData.filter(item => {
      if (!keyword) return true;
      return [item.productName, item.sellerName].some(v => `${v || ''}`.toLowerCase().includes(keyword));
    });
    this.totalRecords = this.filteredData.length;
    this.totalPages = Math.max(1, Math.ceil(this.totalRecords / this.pageSize));
    if (this.currentPage > this.totalPages) this.currentPage = this.totalPages;
    if (this.gridApi) {
      this.gridApi.setRowData(this.filteredData);
      this.gridApi.paginationGoToPage(this.currentPage - 1);
    }
  }

  onSearch(): void {
    this.currentPage = 1;
    this.applyFilter();
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

  private formatCurrency(value: number): string {
    return `${new Intl.NumberFormat('vi-VN').format(value || 0)}đ`;
  }

  private formatDate(date: string): string {
    return this.datePipe.transform(date, 'HH:mm dd/MM/yyyy') || '';
  }
}
