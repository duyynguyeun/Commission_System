import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LayoutComponent } from './pages/layout/layout.component';
import { AuthGuard } from './core/guards/auth.guard';
import { OrderDetailCreateComponent } from './pages/order-detail-create/order-detail-create.component';
import { SaleHistoryComponent } from './pages/sale-history/sale-history.component';
import { SaleOverviewComponent } from './pages/sale-overview/sale-overview.component';
import { AdminProductRevenueComponent } from './pages/admin-product-revenue/admin-product-revenue.component';
import { AdminCompanyRevenueComponent } from './pages/admin-company-revenue/admin-company-revenue.component';
import { AdminEmployeeRevenueComponent } from './pages/admin-employee-revenue/admin-employee-revenue.component';
import { ProductListComponent } from './pages/product-list/product-list.component';
import { ProductDetailComponent } from './pages/product-detail/product-detail.component';
import { AdminProductCrudComponent } from './pages/admin-product-crud/admin-product-crud.component';
import { AdminUserCrudComponent } from './pages/admin-user-crud/admin-user-crud.component';
import { SaleProductListComponent } from './pages/sale-product-list/sale-product-list.component';
import { AdminCommissionPolicyComponent } from './pages/admin-commission-policy/admin-commission-policy.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  {
    path: 'app',
    component: LayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'sale-products', component: SaleProductListComponent },
      { path: 'sale/history', component: SaleHistoryComponent },
      { path: 'sale/overview', component: SaleOverviewComponent },
      { path: 'admin/product-management', component: AdminProductCrudComponent },
      { path: 'admin/user-management', component: AdminUserCrudComponent },
      { path: 'admin/products', component: AdminProductRevenueComponent },
      { path: 'admin/company', component: AdminCompanyRevenueComponent },
      { path: 'admin/employees', component: AdminEmployeeRevenueComponent },
      { path: 'admin/commission-policy', component: AdminCommissionPolicyComponent },
      { path: 'products', component: ProductListComponent },
      { path: 'products/:id', component: ProductDetailComponent },
      { path: '', redirectTo: 'products', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
