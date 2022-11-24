import { ChartModule } from 'primeng/chart';
import { TableModule } from 'primeng/table';
import { MessagesModule } from 'primeng/messages';
import { DialogModule, PanelModule, InputTextModule, ConfirmDialogModule, DropdownModule, ProgressSpinnerModule, AutoCompleteModule, Dialog, CalendarModule, RadioButtonModule } from 'primeng';

import { FormsModule } from '@angular/forms';
import { FormReservationComponent } from './reservation-box/form-reservation/form-reservation.component';
import { SchedulerModule } from './../../shared/scheduler/scheduler.module';
import { HeaderBreadCrumbModule } from './../../shared/layout/header-breadcrumb/header-breadcrumb.module';
import { ReservationPisteComponent } from './reservation-piste/reservation-piste.component';
import { ReservationLitComponent } from './reservation-lit/reservation-lit.component';
import { ReservationBoxComponent } from './reservation-box/reservation-box.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/core/gaurds/auth.gaurd';
import { MatDatepicker, MatDatepickerModule, MatDialogModule, MatFormFieldModule, MatSelectModule } from '@angular/material';
import {ToastModule} from 'primeng/toast';

const routes: Routes = [

  {
    path: 'box',
    component: ReservationBoxComponent,
    data: { breadcrumb: 'Réservation box' },
    canActivate: [AuthGuard]
  },
  {
    path: 'lit',
    component: ReservationLitComponent,
    data: { breadcrumb: 'Réservation lit' },
    canActivate: [AuthGuard]
  },
  {
    path: 'piste',
    component: ReservationPisteComponent,
    data: { breadcrumb: 'Réservation piste' },
    canActivate: [AuthGuard]
  }

];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    HeaderBreadCrumbModule,
    SchedulerModule,
    MatDatepickerModule,
    MatDialogModule,
    MatFormFieldModule,
    MatSelectModule,
    FormsModule,
    DialogModule,
    InputTextModule,
    ConfirmDialogModule,
    MessagesModule,
    DropdownModule,
    ProgressSpinnerModule,
    AutoCompleteModule,
    PanelModule,
    CalendarModule,
    TableModule,
    ConfirmDialogModule,
    ChartModule,
    RadioButtonModule,
    ToastModule
  ],
  declarations: [
    ReservationPisteComponent,
    ReservationBoxComponent,
    ReservationLitComponent,
    FormReservationComponent
  ],
  exports: [
    RouterModule
  ]
})
export class ReservationModule { }
