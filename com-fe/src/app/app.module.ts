import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AgGridModule } from 'ag-grid-angular';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LayoutComponent } from './pages/layout/layout.component';
import { OrderDetailCreateComponent } from './pages/order-detail-create/order-detail-create.component';
import { SaleHistoryComponent } from './pages/sale-history/sale-history.component';
import { SaleOverviewComponent } from './pages/sale-overview/sale-overview.component';
import { AdminProductRevenueComponent } from './pages/admin-product-revenue/admin-product-revenue.component';
import { AdminCompanyRevenueComponent } from './pages/admin-company-revenue/admin-company-revenue.component';
import { AdminEmployeeRevenueComponent } from './pages/admin-employee-revenue/admin-employee-revenue.component';
import { LoginComponent } from './pages/login/login.component';
import { HomeComponent } from './pages/home/home.component';
import { RegisterDialogComponent } from './pages/register-dialog/register-dialog.component';
import { AuthInterceptor } from './core/guards/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    LayoutComponent,
    OrderDetailCreateComponent,
    SaleHistoryComponent,
    SaleOverviewComponent,
    AdminProductRevenueComponent,
    AdminCompanyRevenueComponent,
    AdminEmployeeRevenueComponent,
    LoginComponent,
    HomeComponent
    ,RegisterDialogComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatDialogModule,
    MatButtonModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AgGridModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent],
  entryComponents: [RegisterDialogComponent, LoginComponent]
})
export class AppModule { }
