import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { Product } from '../../core/models/api.model';
import { Router } from '@angular/router';
import { ColDef, GridApi, GridReadyEvent, RowClickedEvent } from 'ag-grid-community';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {
  products: Product[] = [];
  loading = false;
  gridApi!: GridApi;
  
  currentPage = 1;
  totalPages = 1;
  totalRecords = 0;
  Math = Math;

  columnDefs: ColDef[] = [
    { 
      headerName: 'STT', 
      width: 70, 
      valueGetter: 'node.rowIndex + 1',
      cellStyle: { textAlign: 'center', fontWeight: '500' },
      headerClass: 'custom-header-center'
    },
    { 
      field: 'id', 
      headerName: 'MÃ SẢN PHẨM', 
      width: 150, 
      cellStyle: { color: '#3b82f6', fontWeight: '500' } // Blue text like image
    },
    { field: 'name', headerName: 'TÊN SẢN PHẨM', flex: 1 },
    { 
      field: 'price', 
      headerName: 'GIÁ SẢN PHẨM', 
      width: 150, 
      valueFormatter: (params) => {
        return new Intl.NumberFormat('vi-VN').format(params.value) + ' đ';
      }
    },
    { field: 'stockQuantity', headerName: 'TỒN KHO', width: 150 }
  ];

  constructor(private api: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  onGridReady(params: GridReadyEvent) {
    this.gridApi = params.api;
  }

  loadProducts(): void {
    this.loading = true;
    this.api.getProducts(0, 1000).subscribe({
      next: (res) => {
        this.products = res?.data?.data || [];
        this.totalRecords = this.products.length;
        this.totalPages = Math.ceil(this.totalRecords / 10);
        this.loading = false;
      },
      error: () => this.loading = false
    });
  }

  getPages(): number[] {
    const pages: number[] = [];
    for (let i = 1; i <= this.totalPages; i++) {
      pages.push(i);
    }
    // Simplistic pagination logic for demo, usually we'd add ellipses logic
    return pages;
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.gridApi.paginationGoToPage(page - 1);
    }
  }

  onRowClicked(event: RowClickedEvent) {
    if (event.data) {
      this.router.navigate(['/app/products', event.data.id]);
    }
  }
}
