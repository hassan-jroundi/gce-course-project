import {UtilisateursComponent} from './utilisateurs.component';
import {HeaderBreadCrumbModule} from '../../shared/layout/header-breadcrumb/header-breadcrumb.module';
import {ToastModule} from 'primeng/toast';
import {MessagesModule} from 'primeng/messages';
import {FormsModule} from '@angular/forms';
import {TableModule} from 'primeng/table';
import {
  AutoCompleteModule,
  ButtonModule,
  CheckboxModule,
  ConfirmDialogModule,
  DialogModule,
  DropdownModule,
  InputTextModule,
  PanelModule,
  ProgressSpinnerModule,
  TabViewModule
} from 'primeng';
import {AppCommonModule} from '../../app.common.module';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UtilisateursRoutingModule} from './utilisateurs.routing';

@NgModule({
  imports: [
    CommonModule,
    UtilisateursRoutingModule,
    AppCommonModule,
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
    HeaderBreadCrumbModule
  ],
  declarations: [UtilisateursComponent]
})
export class UtilisateursModule {
}
