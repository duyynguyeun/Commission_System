import { Component, OnInit } from '@angular/core';
import { ColDef, GridApi, GridReadyEvent, CellClickedEvent } from 'ag-grid-community';
import { ApiService } from '../../core/services/api.service';
import { Customer, CustomerReq, Employee, EmployeeReq } from '../../core/models/api.model';

type ManagedUserType = 'CUSTOMER' | 'SALE_PARENT' | 'SALE_CHILD' | 'ADMIN';

interface ManagedUserRow {
  id: number;
  entityType: 'CUSTOMER' | 'EMPLOYEE';
  username: string;
  fullName: string;
  role: ManagedUserType;
  parentId?: number | null;
  dob?: string;
  address?: string;
  accountId: number;
  createdAt?: string;
  updatedAt?: string;
}

@Component({
  selector: 'app-admin-user-crud',
  templateUrl: './admin-user-crud.component.html',
  styleUrls: ['./admin-user-crud.component.scss']
})
export class AdminUserCrudComponent implements OnInit {
  users: ManagedUserRow[] = [];
  filteredUsers: ManagedUserRow[] = [];
  loading = false;
  saving = false;
  message = '';
  parents: Employee[] = [];
  searchTerm = '';
  roleFilter = 'ALL';
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
  editingEntityType: 'CUSTOMER' | 'EMPLOYEE' = 'CUSTOMER';
  form = this.createEmptyForm();
  gridHeight = '520px';

  private readonly roleBadge: Record<string, { bg: string; color: string; label: string }> = {
    ADMIN: { bg: '#fef3c7', color: '#b45309', label: 'Admin' },
    SALE_PARENT: { bg: '#dbeafe', color: '#1d4ed8', label: 'Sale Parent' },
    SALE_CHILD: { bg: '#e0e7ff', color: '#4338ca', label: 'Sale Child' },
    CUSTOMER: { bg: '#dcfce7', color: '#16a34a', label: 'Customer' }
  };

  columnDefs: ColDef[] = [
    { headerName: 'STT', width: 70, sortable: false, filter: false, valueGetter: 'node.rowIndex + 1', cellClass: 'cell-center', headerClass: 'header-center' },
    {
      headerName: '', field: 'fullName', width: 55, sortable: false, filter: false, cellClass: 'cell-center',
      cellRenderer: (params: any) => {
        const n = params.value || '?', ini = n.charAt(0).toUpperCase();
        const c = ['#ea580c','#2563eb','#7c3aed','#0891b2','#16a34a','#dc2626'][n.charCodeAt(0)%6];
        return `<div class="user-avatar-cell" style="background:${c}15;color:${c}">${ini}</div>`;
      }
    },
    { field: 'username', headerName: 'Tên đăng nhập', minWidth: 160, flex: 1, cellClass: 'cell-name' },
    { field: 'fullName', headerName: 'Họ và tên', minWidth: 180, flex: 1.2 },
    {
      field: 'role', headerName: 'Vai trò', width: 140, cellClass: 'cell-center', headerClass: 'header-center',
      cellRenderer: (params: any) => {
        const b = this.roleBadge[params.value] || { bg: '#f3f4f6', color: '#6b7280', label: params.value };
        return `<span class="role-badge" style="background:${b.bg};color:${b.color}">${b.label}</span>`;
      }
    },
    { field: 'address', headerName: 'Địa chỉ', minWidth: 160, flex: 1, cellClass: 'cell-desc' },
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
  ngOnInit(): void {
    this.loadUsers();
    this.loadParents();
  }

  loadParents(): void {
    this.api.getEmployeesByRole('SALE_PARENT').subscribe({
      next: r => this.parents = r.data || [],
      error: () => console.error('Failed to fetch parents')
    });
  }

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

  loadUsers(): void {
    this.loading = true; this.message = '';
    let employees: Employee[] = [], customers: Customer[] = [], done = 0;
    const fin = () => {
      if (++done < 2) return;
      this.users = [
        ...employees.map(e => ({ id: e.id, entityType: 'EMPLOYEE' as const, username: e.username, fullName: e.fullName, role: e.role, parentId: e.parentId, accountId: e.accountId, createdAt: e.createAt, updatedAt: e.updateAt, address: '' })),
        ...customers.map(c => ({ id: c.id, entityType: 'CUSTOMER' as const, username: c.username, fullName: c.fullName, role: 'CUSTOMER' as const, parentId: null, accountId: c.accountId, createdAt: c.createdAt, updatedAt: c.updatedAt, dob: c.dob, address: c.address }))
      ];
      this.loading = false; this.applyFilter();
    };
    this.api.getEmployees(0, 1000).subscribe({ next: r => { employees = r.data?.data || []; fin(); }, error: () => fin() });
    this.api.getCustomers(0, 1000).subscribe({ next: r => { customers = r.data?.data || []; fin(); }, error: () => fin() });
  }

  applyFilter(): void {
    const kw = this.searchTerm.trim().toLowerCase();
    this.filteredUsers = this.users.filter(u => {
      const mk = !kw || [u.username, u.fullName, u.role, u.address].some(v => `${v||''}`.toLowerCase().includes(kw));
      return mk && (this.roleFilter === 'ALL' || u.role === this.roleFilter);
    });
    this.totalRecords = this.filteredUsers.length;
    this.totalPages = Math.max(1, Math.ceil(this.totalRecords / this.pageSize));
    if (this.currentPage > this.totalPages) this.currentPage = this.totalPages;
    if (this.gridApi) { this.gridApi.setRowData(this.filteredUsers); this.gridApi.paginationGoToPage(this.currentPage - 1); }
  }

  onSearch(): void { this.currentPage = 1; this.applyFilter(); }

  toggleForm(): void {
    if (this.showForm && !this.isEditMode) this.closeForm();
    else { this.showForm = true; this.isEditMode = false; this.editingId = null; this.form = this.createEmptyForm(); this.message = ''; }
  }

  closeForm(): void { this.showForm = false; this.isEditMode = false; this.editingId = null; this.form = this.createEmptyForm(); this.message = ''; }

  submit(): void {
    if (!this.form.username.trim() || !this.form.fullName.trim()) { this.message = 'Tên đăng nhập và họ tên là bắt buộc.'; return; }
    if (!this.form.password.trim()) { this.message = 'Mật khẩu là bắt buộc.'; return; }
    if (this.form.role === 'SALE_CHILD' && !this.form.parentId) { this.message = 'SALE_CHILD phải có Parent ID.'; return; }
    if (this.form.role === 'CUSTOMER' && (!this.form.dob || !this.form.address.trim())) { this.message = 'Customer phải có ngày sinh và địa chỉ.'; return; }
    this.saving = true; this.message = '';

    if (this.form.role === 'CUSTOMER') {
      const p: CustomerReq = { username: this.form.username.trim(), password: this.form.password.trim(), fullName: this.form.fullName.trim(), dob: this.form.dob, address: this.form.address.trim(), role: 'CUSTOMER' };
      (this.isEditMode && this.editingId ? this.api.updateCustomer(this.editingId, p) : this.api.createCustomer(p))
        .subscribe({ next: () => this.onSaved(), error: e => this.onSaveError(e) });
      return;
    }
    const p: EmployeeReq = { username: this.form.username.trim(), password: this.form.password.trim(), fullName: this.form.fullName.trim(), role: this.form.role as any, parentId: this.form.role === 'SALE_CHILD' ? Number(this.form.parentId) : null };
    (this.isEditMode && this.editingId ? this.api.updateEmployee(this.editingId, p) : this.api.createEmployee(p))
      .subscribe({ next: () => this.onSaved(), error: e => this.onSaveError(e) });
  }

  startEdit(user: ManagedUserRow): void {
    this.isEditMode = true; this.editingId = user.id; this.editingEntityType = user.entityType; this.showForm = true; this.message = '';
    this.form = { role: user.role, username: user.username, password: '', fullName: user.fullName, parentId: user.parentId || null, dob: user.dob || '', address: user.address || '' };
  }

  remove(user: ManagedUserRow): void {
    if (!window.confirm(`Xóa tài khoản "${user.username}"?`)) return;
    (user.entityType === 'CUSTOMER' ? this.api.deleteCustomer(user.id) : this.api.deleteEmployee(user.id)).subscribe({
      next: () => { this.showToast('Xóa người dùng thành công!'); if (this.editingId === user.id) this.closeForm(); this.loadUsers(); },
      error: e => this.showToast(e?.error?.message || 'Không thể xóa người dùng.', 'error')
    });
  }

  onActionClick(event: CellClickedEvent): void {
    const action = (event.event?.target as HTMLElement)?.getAttribute('data-action');
    if (!action || !event.data) return;
    if (action === 'edit') this.startEdit(event.data as ManagedUserRow);
    if (action === 'delete') this.remove(event.data as ManagedUserRow);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    if (this.gridApi) this.gridApi.paginationGoToPage(page - 1);
  }

  getPages(): (number | string)[] {
    const t = this.totalPages, c = this.currentPage;
    if (t <= 7) return Array.from({ length: t }, (_, i) => i + 1);
    const p: (number | string)[] = [1];
    if (c > 3) p.push('...');
    for (let i = Math.max(2, c - 1); i <= Math.min(t - 1, c + 1); i++) p.push(i);
    if (c < t - 2) p.push('...');
    p.push(t);
    return p;
  }

  private onSaved(): void { this.saving = false; this.showToast(this.isEditMode ? 'Cập nhật người dùng thành công!' : 'Tạo người dùng thành công!'); this.closeForm(); this.loadUsers(); }
  private onSaveError(e: any): void { this.saving = false; this.message = e?.error?.message || 'Không thể lưu người dùng.'; }

  private createEmptyForm() {
    return { role: 'CUSTOMER' as ManagedUserType, username: '', password: '', fullName: '', parentId: null as number | null, dob: '', address: '' };
  }
}
