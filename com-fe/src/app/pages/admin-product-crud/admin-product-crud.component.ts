import { Component, OnInit } from '@angular/core';
import { ColDef, GridApi, GridReadyEvent, CellClickedEvent } from 'ag-grid-community';
import { ApiService } from '../../core/services/api.service';
import { Product, ProductReq } from '../../core/models/api.model';

@Component({
  selector: 'app-admin-product-crud',
  templateUrl: './admin-product-crud.component.html',
  styleUrls: ['./admin-product-crud.component.scss']
})
export class AdminProductCrudComponent implements OnInit {
  products: Product[] = [];
  filteredProducts: Product[] = [];
  loading = false;
  saving = false;
  message = '';
  searchTerm = '';
  gridApi!: GridApi;
  showForm = false;

  toastMessage = '';
  toastType: 'success' | 'error' = 'success';
  private toastTimeout: any;

  currentPage = 1;
  pageSize = 10;
  totalPages = 1;
  totalRecords = 0;
  Math = Math;

  isEditMode = false;
  editingId: number | null = null;
  form: ProductReq = this.createEmptyForm();

  gridHeight = '520px';

  columnDefs: ColDef[] = [
    {
      headerName: 'STT', width: 70, sortable: false, filter: false,
      valueGetter: 'node.rowIndex + 1',
      cellClass: 'cell-center', headerClass: 'header-center'
    },
    {
      headerName: 'Ảnh', field: 'urlImage', width: 80, sortable: false, filter: false,
      cellClass: 'cell-center', headerClass: 'header-center',
      cellRenderer: (params: any) => {
        const url = params.value;
        if (url) return `<img src="${url}" class="grid-thumb" alt="" />`;
        return `<div class="grid-thumb-placeholder"><i class='bx bx-image'></i></div>`;
      }
    },
    { field: 'name', headerName: 'Tên sản phẩm', flex: 1.5, minWidth: 200, cellClass: 'cell-name' },
    {
      field: 'price', headerName: 'Giá', width: 150, cellClass: 'cell-price',
      valueFormatter: params => this.formatCurrency(params.value)
    },
    { field: 'stockQuantity', headerName: 'Tồn kho', width: 100, cellClass: 'cell-center', headerClass: 'header-center' },
    { field: 'description', headerName: 'Mô tả', flex: 1.2, minWidth: 180, cellClass: 'cell-desc' },
    {
      headerName: 'Hành động', field: 'actions', width: 120, sortable: false, filter: false,
      cellClass: 'cell-center', headerClass: 'header-center',
      cellRenderer: () => `
        <div class="grid-actions">
          <button class="action-btn edit-btn" data-action="edit" title="Sửa"><i class='bx bx-edit-alt' data-action="edit"></i></button>
          <button class="action-btn delete-btn" data-action="delete" title="Xóa"><i class='bx bx-trash' data-action="delete"></i></button>
        </div>`
    }
  ];

  constructor(private api: ApiService) {}

  ngOnInit(): void { this.loadProducts(); }

  onGridReady(params: GridReadyEvent): void {
    this.gridApi = params.api;
    this.gridApi.paginationSetPageSize(this.pageSize);
  }

  showToast(msg: string, type: 'success' | 'error' = 'success'): void {
    if (this.toastTimeout) clearTimeout(this.toastTimeout);
    this.toastMessage = msg;
    this.toastType = type;
    this.toastTimeout = setTimeout(() => { this.toastMessage = ''; }, 3000);
  }

  loadProducts(): void {
    this.loading = true;
    this.api.getProducts(0, 1000).subscribe({
      next: res => { this.products = res.data?.data || []; this.applyFilter(); this.loading = false; },
      error: () => { this.loading = false; this.showToast('Không thể tải danh sách sản phẩm.', 'error'); }
    });
  }

  applyFilter(): void {
    const keyword = this.searchTerm.trim().toLowerCase();
    this.filteredProducts = this.products.filter(p => {
      if (!keyword) return true;
      return [p.id, p.name, p.description].some(v => `${v || ''}`.toLowerCase().includes(keyword));
    });
    this.totalRecords = this.filteredProducts.length;
    this.totalPages = Math.max(1, Math.ceil(this.totalRecords / this.pageSize));
    if (this.currentPage > this.totalPages) this.currentPage = this.totalPages;
    if (this.gridApi) {
      this.gridApi.setRowData(this.filteredProducts);
      this.gridApi.paginationGoToPage(this.currentPage - 1);
    }
  }

  onSearch(): void { this.currentPage = 1; this.applyFilter(); }

  toggleForm(): void {
    if (this.showForm && !this.isEditMode) { this.closeForm(); }
    else { this.showForm = true; this.isEditMode = false; this.editingId = null; this.form = this.createEmptyForm(); this.message = ''; }
  }

  closeForm(): void {
    this.showForm = false; this.isEditMode = false; this.editingId = null; this.form = this.createEmptyForm(); this.message = '';
  }

  submit(): void {
    if (!this.form.name.trim()) { this.message = 'Tên sản phẩm là bắt buộc.'; return; }
    if (this.form.price <= 0 || this.form.stockQuantity < 0) { this.message = 'Giá và tồn kho phải hợp lệ.'; return; }
    this.saving = true; this.message = '';
    const request = { ...this.form, name: this.form.name.trim(), description: (this.form.description || '').trim(), urlImage: (this.form.urlImage || '').trim() };
    const op = this.isEditMode && this.editingId ? this.api.updateProduct(this.editingId, request) : this.api.createProduct(request);
    op.subscribe({
      next: () => { this.saving = false; this.showToast(this.isEditMode ? 'Cập nhật sản phẩm thành công!' : 'Thêm sản phẩm thành công!'); this.closeForm(); this.loadProducts(); },
      error: err => { this.saving = false; this.message = err?.error?.message || 'Không thể lưu sản phẩm.'; }
    });
  }

  startEdit(product: Product): void {
    this.isEditMode = true; this.editingId = product.id; this.showForm = true; this.message = '';
    this.form = { name: product.name || '', price: Number(product.price) || 0, stockQuantity: product.stockQuantity || 0, urlImage: product.urlImage || '', description: product.description || '' };
  }

  remove(product: Product): void {
    if (!window.confirm(`Xóa sản phẩm "${product.name}"?`)) return;
    this.api.deleteProduct(product.id).subscribe({
      next: () => { this.showToast('Xóa sản phẩm thành công!'); if (this.editingId === product.id) this.closeForm(); this.loadProducts(); },
      error: err => this.showToast(err?.error?.message || 'Không thể xóa sản phẩm.', 'error')
    });
  }

  onActionClick(event: CellClickedEvent): void {
    const target = event.event?.target as HTMLElement | null;
    const action = target?.getAttribute('data-action');
    if (!action || !event.data) return;
    if (action === 'edit') this.startEdit(event.data as Product);
    if (action === 'delete') this.remove(event.data as Product);
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

  private createEmptyForm(): ProductReq {
    return { name: '', price: 0, stockQuantity: 0, urlImage: '', description: '' };
  }

  private formatCurrency(value: number): string {
    return `${new Intl.NumberFormat('vi-VN').format(value || 0)}đ`;
  }
}
