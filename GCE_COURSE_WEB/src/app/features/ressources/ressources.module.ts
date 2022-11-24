import { MessagesModule } from 'primeng/messages';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { HeaderBreadCrumbModule } from './../../shared/layout/header-breadcrumb/header-breadcrumb.module';
import { RessourcesPisteComponent } from './ressources-piste/ressources-piste.component';
import { RessourcesImmeubleComponent } from './ressources-immeuble/ressources-immeuble.component';
import { RessourcesEcurieComponent } from './ressources-ecurie/ressources-ecurie.component';
import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthGuard } from 'src/app/core/gaurds/auth.gaurd';
import { PanelModule, ButtonModule, DialogModule, CheckboxModule, InputTextModule, ConfirmDialogModule, MessageModule, DropdownModule, ProgressSpinnerModule, AutoCompleteModule, SelectButtonModule } from 'primeng';

const routes: Routes = [

  {
    path: 'immeuble',
    component: RessourcesImmeubleComponent,
    data: { breadcrumb: 'Immeuble' },
    canActivate: [AuthGuard]
  },
  {
    path: 'ecurie',
    component: RessourcesEcurieComponent,
    data: { breadcrumb: 'Ecurie' },
    canActivate: [AuthGuard]
  },
  {
    path: 'piste',
    component: RessourcesPisteComponent,
    data: { breadcrumb: 'Piste' },
    canActivate: [AuthGuard]
  }

];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    HeaderBreadCrumbModule,
    TableModule,
    FormsModule,
    PanelModule,
    ButtonModule,
    DialogModule,
    CheckboxModule,
    InputTextModule,
    ConfirmDialogModule,
    ToastModule,
    MessagesModule,
    DropdownModule,
    ProgressSpinnerModule,
    AutoCompleteModule,
    SelectButtonModule,
    ConfirmDialogModule,
    PanelModule
  ],
  declarations: [
    RessourcesEcurieComponent,
    RessourcesImmeubleComponent,
    RessourcesPisteComponent
  ],
  exports: [
    RouterModule
  ],
  providers: [
    MessageService,
    ConfirmationService
  ]
})
export class RessourcesModule { }
