import { AlphasOnlyDirective } from './../../core/validators/alphasonly.directive';
import { ToastModule } from 'primeng/toast';
import { MessagesModule } from 'primeng/messages';
import { FormsModule, FormControl } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { HeaderBreadCrumbModule } from './../../shared/layout/header-breadcrumb/header-breadcrumb.module';
import { ConsultationChevalComponent } from './../consultation/consultation-cheval/consultation-cheval.component';
import { PanelModule } from 'primeng/panel';
import { ClientelePersonneComponent } from './clientele-personne/clientele-personne.component';
import { ClienteleChevalComponent } from './clientele-cheval/clientele-cheval.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TabViewModule} from 'primeng/tabview';
import { AuthGuard } from 'src/app/core/gaurds/auth.gaurd';
import { Routes, RouterModule } from '@angular/router';
import { ButtonModule, DialogModule, CheckboxModule, InputTextModule, ConfirmDialogModule, ProgressSpinnerModule, DropdownModule, AutoCompleteModule, MessageService, ConfirmationService } from 'primeng';

const routes: Routes = [

  {
    path: 'cheval',
    component: ClienteleChevalComponent,
    data: { breadcrumb: 'Clientèle cheval' },
    canActivate: [AuthGuard]
  },
  {
    path: 'personne',
    component: ClientelePersonneComponent,
    data: { breadcrumb: 'Clientèle personne' },
    canActivate: [AuthGuard]
  }

];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    PanelModule,
    HeaderBreadCrumbModule,
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
    PanelModule,
    ConfirmDialogModule

  ],
  declarations: [
    ClientelePersonneComponent,
    ClienteleChevalComponent,
    AlphasOnlyDirective
  ],
  exports: [
    RouterModule
  ],
  providers: [
    MessageService,
    ConfirmationService
  ]
})
export class ClienteleModule { }
