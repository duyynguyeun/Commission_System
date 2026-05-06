import { Component, OnInit } from '@angular/core';
import { ColDef, GridApi, GridReadyEvent, CellClickedEvent } from 'ag-grid-community';
import { ApiService } from '../../core/services/api.service';
import { Product, CommissionPolicy, CommissionPolicyReq } from '../../core/models/api.model';

interface PolicyRow {
  productId: number;
  productName: string;
  productPrice: number;
  policyId: number | null;
  companyRate: number | null;
  parentRate: number | null;
  childRate: number | null;
}

@Component({
  selector: 'app-admin-commission-policy',
  templateUrl: './admin-commission-policy.component.html',
  styleUrls: ['./admin-commission-policy.component.scss']
})
export class AdminCommissionPolicyComponent implements OnInit {
  rows: PolicyRow[] = [];
  filteredRows: PolicyRow[] = [];
  loading = false;
  saving = false;
  searchTerm = '';
  gridApi!: GridApi;
  gridHeight = '520px';

  toastMessage = '';
  toastType: 'success' | 'error' = 'success';
  private toastTimeout: any;

  currentPage = 1;
  pageSize = 10;
  totalPages = 1;
  totalRecords = 0;
  Math = Math;

  // Edit popup
  showEditPopup = false;
  editingRow: PolicyRow | null = null;
  editForm = { companyRate: 0, parentRate: 0, childRate: 0 };
  editMessage = '';

  columnDefs: ColDef[] = [
    {
      headerName: 'STT', width: 70, sortable: false, filter: false,
      valueGetter: 'node.rowIndex + 1',
      cellClass: 'cell-center', headerClass: 'header-center'
    },
    { field: 'productName', headerName: 'Tên sản phẩm', flex: 1.5, minWidth: 200, cellClass: 'cell-name' },
    {
      field: 'productPrice', headerName: 'Giá bán', width: 150, cellClass: 'cell-price',
      valueFormatter: params => this.formatCurrency(params.value)
    },
    {
      field: 'companyRate', headerName: 'Lợi nhuận công ty', width: 170,
      cellClass: 'cell-center', headerClass: 'header-center',
      cellRenderer: (params: any) => {
        const rate = params.value;
        if (rate == null) return `<span class="rate-badge rate-empty">Chưa thiết lập</span>`;
        const price = params.data?.productPrice || 0;
        const amount = (price * rate / 100);
        return `<span class="rate-badge rate-company">${this.formatCurrency(amount)} <small>(${rate}%)</small></span>`;
      }
    },
    {
      field: 'parentRate', headerName: 'Sale cấp 1', width: 160,
      cellClass: 'cell-center', headerClass: 'header-center',
      cellRenderer: (params: any) => {
        const rate = params.value;
        if (rate == null) return `<span class="rate-badge rate-empty">Chưa thiết lập</span>`;
        const price = params.data?.productPrice || 0;
        const amount = (price * rate / 100);
        return `<span class="rate-badge rate-parent">${this.formatCurrency(amount)} <small>(${rate}%)</small></span>`;
      }
    },
    {
      field: 'childRate', headerName: 'Sale cấp 2', width: 160,
      cellClass: 'cell-center', headerClass: 'header-center',
      cellRenderer: (params: any) => {
        const rate = params.value;
        if (rate == null) return `<span class="rate-badge rate-empty">Chưa thiết lập</span>`;
        const price = params.data?.productPrice || 0;
        const amount = (price * rate / 100);
        return `<span class="rate-badge rate-child">${this.formatCurrency(amount)} <small>(${rate}%)</small></span>`;
      }
    },
    {
      headerName: '', field: 'actions', width: 100, sortable: false, filter: false,
      cellClass: 'cell-center', headerClass: 'header-center',
      cellRenderer: () => `
        <div class="grid-actions">
          <button class="action-btn edit-btn" data-action="edit" title="Sửa"><i class='bx bx-edit-alt' data-action="edit"></i></button>
        </div>`
    }
  ];

  constructor(private api: ApiService) {}

  ngOnInit(): void { this.loadData(); }

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

  loadData(): void {
    this.loading = true;
    let products: Product[] = [];
    let policies: CommissionPolicy[] = [];
    let done = 0;

    const finish = () => {
      if (++done < 2) return;
      // Build a map of productId -> policy
      const policyMap = new Map<number, CommissionPolicy>();
      policies.forEach(p => policyMap.set(p.productId, p));

      this.rows = products.map(prod => {
        const policy = policyMap.get(prod.id);
        return {
          productId: prod.id,
          productName: prod.name,
          productPrice: prod.price,
          policyId: policy?.id ?? null,
          companyRate: policy?.companyRate ?? null,
          parentRate: policy?.parentRate ?? null,
          childRate: policy?.childRate ?? null
        };
      });
      this.loading = false;
      this.applyFilter();
    };

    this.api.getProducts(0, 1000).subscribe({
      next: res => { products = res.data?.data || []; finish(); },
      error: () => { finish(); }
    });
    this.api.getCommissionPolicies(0, 1000).subscribe({
      next: res => { policies = res.data?.data || []; finish(); },
      error: () => { finish(); }
    });
  }

  applyFilter(): void {
    const keyword = this.searchTerm.trim().toLowerCase();
    this.filteredRows = this.rows.filter(r => {
      if (!keyword) return true;
      return `${r.productName || ''}`.toLowerCase().includes(keyword);
    });
    this.totalRecords = this.filteredRows.length;
    this.totalPages = Math.max(1, Math.ceil(this.totalRecords / this.pageSize));
    if (this.currentPage > this.totalPages) this.currentPage = this.totalPages;
    if (this.gridApi) {
      this.gridApi.setRowData(this.filteredRows);
      this.gridApi.paginationGoToPage(this.currentPage - 1);
    }
  }

  onSearch(): void { this.currentPage = 1; this.applyFilter(); }

  onActionClick(event: CellClickedEvent): void {
    const target = event.event?.target as HTMLElement | null;
    const action = target?.getAttribute('data-action');
    if (!action || !event.data) return;
    if (action === 'edit') this.openEditPopup(event.data as PolicyRow);
  }

  openEditPopup(row: PolicyRow): void {
    this.editingRow = row;
    this.editForm = {
      companyRate: row.companyRate ?? 80,
      parentRate: row.parentRate ?? 15,
      childRate: row.childRate ?? 5
    };
    this.editMessage = '';
    this.showEditPopup = true;
  }

  closeEditPopup(): void {
    this.showEditPopup = false;
    this.editingRow = null;
    this.editMessage = '';
  }

  submitEdit(): void {
    if (!this.editingRow) return;

    const total = Number(this.editForm.companyRate) + Number(this.editForm.parentRate) + Number(this.editForm.childRate);
    if (total !== 100) {
      this.editMessage = `Tổng tỷ lệ phải bằng 100% (hiện tại: ${total}%)`;
      return;
    }

    this.saving = true;
    this.editMessage = '';
    const payload: CommissionPolicyReq = {
      companyRate: Number(this.editForm.companyRate),
      parentRate: Number(this.editForm.parentRate),
      childRate: Number(this.editForm.childRate),
      productId: this.editingRow.productId
    };

    const request = this.editingRow.policyId
      ? this.api.updateCommissionPolicy(this.editingRow.policyId, payload)
      : this.api.createCommissionPolicy(payload);

    request.subscribe({
      next: () => {
        this.saving = false;
        this.showToast('Cập nhật chính sách hoa hồng thành công!');
        this.closeEditPopup();
        this.loadData();
      },
      error: err => {
        this.saving = false;
        this.editMessage = err?.error?.message || 'Không thể cập nhật chính sách hoa hồng.';
      }
    });
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
}
