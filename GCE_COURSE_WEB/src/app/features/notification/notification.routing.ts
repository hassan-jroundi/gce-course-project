import { NotificationComponent } from './notification.component';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import {FacturationPersonneComponent} from '../facturation/facturation-personne/facturation-personne.component';
import {AuthGuard} from '../../core/gaurds/auth.gaurd';

const routes: Routes = [
  {
    path: '',
    canActivate: [AuthGuard],
    component: NotificationComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class NotificationRoutingModule { }
