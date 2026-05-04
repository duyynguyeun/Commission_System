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
  relatedLevelRevenue: number;
  relatedLevelCommission: number;
  totalRevenue: number;
  totalCommission: number;
}

export interface AdminProductRevenue {
  productId: number;
  productName: string;
  revenue: number;
}

export interface AdminEmployeeRevenue {
  employeeId: number;
  employeeName: string;
  salesRevenue: number;
  totalCommission: number;
}

export interface OrderDetailReq {
  price: number;
  quantity: number;
  customerOrderId: number;
  productId: number;
  sellerId: number;
  parentId?: number;
  affiliateLinkId: number;
}
