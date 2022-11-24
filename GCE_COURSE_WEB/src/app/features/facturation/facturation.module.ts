import { RouterModule, Routes } from '@angular/router';
import { FacturationPersonneComponent } from './facturation-personne/facturation-personne.component';
import { ToastModule } from 'primeng/toast';
import { MessagesModule } from 'primeng/messages';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { PanelModule, TabViewModule, ButtonModule, DialogModule, CheckboxModule, InputTextModule, ConfirmDialogModule, ProgressSpinnerModule, DropdownModule, AutoCompleteModule, TreeTableModule } from 'primeng';
import { AppCommonModule } from 'src/app/app.common.module';
import { NgModule } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { HeaderBreadCrumbModule } from 'src/app/shared/layout/header-breadcrumb/header-breadcrumb.module';
import { FacturationMoisComponent } from './facturation-mois/facturation-mois.component';
import { AuthGuard } from 'src/app/core/gaurds/auth.gaurd';

const routes: Routes = [

  {
    path: 'personne',
    component: FacturationPersonneComponent,
    data: { breadcrumb: 'Recherche par personne' },
    canActivate: [AuthGuard]
  },
  {
    path: 'mois',
    component: FacturationMoisComponent,
    data: { breadcrumb: 'Recherche par mois' },
    canActivate: [AuthGuard]
  }

];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    AppCommonModule,
    HeaderBreadCrumbModule,
    PanelModule,
    TabViewModule,
    ButtonModule,
    TableModule,
    FormsModule,
    DialogModule,
    CheckboxModule,
    InputTextModule,
    ConfirmDialogModule,
    MessagesModule,
    ToastModule,
    ProgressSpinnerModule,
    DropdownModule,
    AutoCompleteModule,
    TreeTableModule,
  ],
  declarations: [
    FacturationPersonneComponent,
    FacturationMoisComponent
  ],
  exports: [
    RouterModule
  ],
  providers: [
    DatePipe
  ]
})
export class FacturationModule { }
