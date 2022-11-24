import { MatDatepickerModule, MatDialogModule, MatFormFieldModule, MatSelectModule } from '@angular/material';
import { SchedulerModule } from './../../shared/scheduler/scheduler.module';
import { TableModule } from 'primeng/table';
import { MessagesModule } from 'primeng/messages';
import { HeaderBreadCrumbModule } from './../../shared/layout/header-breadcrumb/header-breadcrumb.module';
import { ConsultationPersonneComponent } from './consultation-personne/consultation-personne.component';
import { ConsultationChevalComponent } from './consultation-cheval/consultation-cheval.component';
import { AuthGuard } from './../../core/gaurds/auth.gaurd';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { AutoCompleteModule, ButtonModule, CalendarModule, CheckboxModule, ConfirmDialogModule, DialogModule, DropdownModule, FieldsetModule, InputTextModule, ListboxModule, MessageModule, MultiSelectModule, PanelModule, ProgressSpinnerModule, TabViewModule, TooltipModule, TreeModule } from 'primeng';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {ToastModule} from 'primeng/toast';

const routes: Routes = [

  {
    path: 'cheval',
    component: ConsultationChevalComponent,
    data: { breadcrumb: 'Consultation cheval' },
    canActivate: [AuthGuard]
  },
  {
    path: 'personne',
    component: ConsultationPersonneComponent,
    data: { breadcrumb: 'Consultation personne' },
    canActivate: [AuthGuard]
  }

];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    FormsModule,
    ReactiveFormsModule,
    ButtonModule,
    CheckboxModule,
    ConfirmDialogModule,
    DialogModule,
    DropdownModule,
    FieldsetModule,
    AutoCompleteModule,
    CalendarModule,
    PanelModule,
    HeaderBreadCrumbModule,
    TabViewModule,
    ProgressSpinnerModule,
    InputTextModule,
    MessagesModule,
    TableModule,
    SchedulerModule,
    MatDatepickerModule,
    MatDialogModule,
    MatFormFieldModule,
    MatSelectModule,
    ToastModule
  ],
  declarations: [
    ConsultationPersonneComponent,
    ConsultationChevalComponent
  ],
  exports: [
    RouterModule
  ]
})
export class ConsultationModule { }
