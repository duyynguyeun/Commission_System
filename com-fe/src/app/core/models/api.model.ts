export type UserRole = 'ADMIN' | 'SALE_PARENT' | 'SALE_CHILD' | 'CUSTOMER';

export interface LoginReq {
  username: string;
  password: string;
}

export interface RegisterReq {
  username: string;
  password: string;
  fullName: string;
  role: UserRole;
  parentId?: number | null;
}

export interface AuthRes {
  accessToken: string;
  tokenType: string;
  accountId: number;
  userId: number;
  username: string;
  role: UserRole;
}

export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

export interface PageResponse<T> {
  pagination: PageMeta;
  data: T[];
}

export interface PageMeta {
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageSize: number;
}

export interface Product {
  id: number;
  name: string;
  price: number;
  stockQuantity: number;
  urlImage: string;
  description: string;
  createAt: string;
  updateAt: string;
}

export interface ProductReq {
  name: string;
  price: number;
  stockQuantity: number;
  urlImage?: string;
  description?: string;
}

export interface CustomerOrderReq {
  totalPrice: number;
  status: 'PENDING' | 'PAID' | 'COMPLETED' | 'CANCEL';
  customerId: number;
}

export interface SaleHistoryItem {
  orderDetailId: number;
  productId: number;
  productName: string;
  quantity: number;
  lineRevenue: number;
  commissionRole: string;
  commissionRate: number;
  commissionAmount: number;
  transactionAt: string;
}

export interface SaleOverview {
  employeeId: number;
  ownRevenue: number;
  ownCommission: number;
  totalPrice: number;
  status: 'PENDING' | 'PAID' | 'COMPLETED' | 'CANCEL';
  customerId: number;
}

export interface SaleHistoryItem {
  orderDetailId: number;
  productId: number;
  productName: string;
  quantity: number;
  lineRevenue: number;
  commissionRole: string;
  commissionRate: number;
  commissionAmount: number;
  sellerName: string;
  transactionAt: string;
}

export interface SaleOverview {
  employeeId: number;
  ownRevenue: number;
  ownCommission: number;
  relatedLevelRevenue: number;
  relatedLevelCommission: number;
  totalRevenue: number;
  totalCommission: number;
}

export interface AdminProductRevenue {
  productId: number;
  productName: string;
  price: number;
  quantitySold: number;
  revenue: number;
}

export interface AdminEmployeeRevenue {
  employeeId: number;
  employeeName: string;
  role: string;
  salesRevenue: number;
  totalCommission: number;
}

export interface AdminCompanyRevenue {
  totalSalesRevenue: number;
  directSalesRevenue: number;
  affiliateSalesRevenue: number;
  totalCommissionPaid: number;
  netCompanyRevenue: number;
}

export interface SaleProduct {
  id: number;
  name: string;
  price: number;
  maxCommissionRate: number;
  maxCommissionAmount: number;
  urlImage: string;
}


export interface Employee {
  id: number;
  parentId?: number | null;
  fullName: string;
  createAt: string;
  updateAt: string;
  accountId: number;
  username: string;
  role: 'SALE_PARENT' | 'SALE_CHILD' | 'ADMIN';
}

export interface Customer {
  id: number;
  fullName: string;
  dob: string;
  address: string;
  createdAt: string;
  updatedAt: string;
  accountId: number;
  username: string;
  role: 'CUSTOMER';
}

export interface EmployeeReq {
  parentId?: number | null;
  username: string;
  password: string;
  fullName: string;
  role: 'SALE_PARENT' | 'SALE_CHILD' | 'ADMIN';
}

export interface CustomerReq {
  username: string;
  password: string;
  fullName: string;
  dob: string;
  address: string;
  role: 'CUSTOMER';
}

export interface OrderDetailReq {
  price: number;
  quantity: number;
  customerOrderId: number;
  productId: number;
  sellerId: number | null;
  parentId?: number | null;
  affiliateLinkId: number | null;
}

export interface AffiliateLinkRenderReq {
  productId: number;
}

export interface AffiliateLinkRes {
  id: number;
  affCode: string;
  affUrl: string;
  productId: number;
  employeeId: number;
  createAt: string;
  updateAt: string;
}

export interface CommissionPolicy {
  id: number;
  companyRate: number;
  parentRate: number;
  childRate: number;
  productId: number;
  productName: string;
  productPrice: number;
  createAt: string;
  updateAt: string;
}

export interface CommissionPolicyReq {
  companyRate: number;
  parentRate: number;
  childRate: number;
  productId: number;
}
