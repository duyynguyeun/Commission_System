import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { SaleProduct } from '../../core/models/api.model';
import { ColDef, GridApi, GridReadyEvent } from 'ag-grid-community';
import { MatDialog } from '@angular/material/dialog';
import { AffiliateLinkDialogComponent } from '../affiliate-link-dialog/affiliate-link-dialog.component';

@Component({
  selector: 'app-sale-product-list',
  templateUrl: './sale-product-list.component.html',
  styleUrls: ['./sale-product-list.component.scss']
})
export class SaleProductListComponent implements OnInit {
  products: SaleProduct[] = [];
  gridApi!: GridApi;
  
  columnDefs: ColDef[] = [
    { 
      headerName: 'STT', 
      valueGetter: (params: any) => params.node.rowIndex + 1 + (this.currentPage - 1) * this.pageSize, 
      width: 70,
      cellStyle: { textAlign: 'center', fontWeight: 'bold', color: '#666' }
    },
    { 
      field: 'name', 
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
      valueFormatter: params => (params.value || 0).toLocaleString() + ' đ'
    },
    { 
      field: 'maxCommissionRate', 
      headerName: 'Hoa hồng tối đa', 
      width: 200,
      cellRenderer: (params: any) => {
        const amount = (params.data.maxCommissionAmount || 0).toLocaleString();
        return `
          <div class="comm-cell">
            <span class="badge badge-success">${params.value}%</span>
            <span class="comm-amt">${amount} đ</span>
          </div>
        `;
      }
    },
    { 
      headerName: 'Hành động', 
      width: 160,
      cellRenderer: () => `
        <button class="btn-aff">
          <i class='bx bx-link-alt'></i> Tạo link Aff
        </button>
      `
    }
  ];

  defaultColDef: ColDef = {
    sortable: true,
    filter: true,
    resizable: true
  };

  currentPage = 1;
  pageSize = 10;
  totalPages = 1;
  totalRecords = 0;
  Math = Math;

  constructor(private api: ApiService, private dialog: MatDialog) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.api.getSaleProducts(this.currentPage - 1, this.pageSize).subscribe(res => {
      if (res.data) {
        this.products = res.data.data;
        this.totalRecords = res.data.pagination.totalElements;
        this.totalPages = res.data.pagination.totalPages;
      }
    });
  }

  onGridReady(params: GridReadyEvent): void {
    this.gridApi = params.api;
    setTimeout(() => params.api.sizeColumnsToFit(), 200);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.loadProducts();
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

  onCellClicked(params: any): void {
    if (params.colDef.headerName === 'Hành động') {
      const productId = params.data.id;
      this.api.renderAffiliateLink({ productId }).subscribe(res => {
        if (res.data) {
          this.dialog.open(AffiliateLinkDialogComponent, {
            data: { affUrl: res.data.affUrl },
            width: '500px'
          });
        }
      });
    }
  }
}
