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

const routes: Routes = [
  { path: '', component: HomeComponent },
  {
    path: 'app',
    component: LayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'order-detail', component: OrderDetailCreateComponent },
      { path: 'sale/history', component: SaleHistoryComponent },
      { path: 'sale/overview', component: SaleOverviewComponent },
      { path: 'admin/products', component: AdminProductRevenueComponent },
      { path: 'admin/company', component: AdminCompanyRevenueComponent },
      { path: 'admin/employees', component: AdminEmployeeRevenueComponent },
      { path: '', redirectTo: 'order-detail', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
