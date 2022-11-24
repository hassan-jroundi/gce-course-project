import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {AuthGuard} from 'src/app/core/gaurds/auth.gaurd';
import {LayoutComponent} from 'src/app/shared/layout/layout.component';
import {ErrorComponent} from './shared/error/error.component';

const routes: Routes = [
  {
    path: 'login',
    loadChildren: () => import('src/app/features/login/login.module').then(m => m.LoginModule)
  },
  {
    path: 'register',
    loadChildren: () => import('src/app/features/register-user/register-user.module').then(m => m.RegisterUserModule)
  },
  {
    path: 'gce',
    loadChildren: () => import('src/app/features/gce/gce.module').then(m => m.GceModule)
  },
  {
    path: 'main',
    component: LayoutComponent,
    children: [{
      path: 'dashboard',
      loadChildren: () => import('src/app/features/dashboard/dashboard.module').then(m => m.DashboardModule),
      canActivate: [AuthGuard]
    },
      {
        path: 'consultation',
        loadChildren: () => import('src/app/features/consultation/consultation.module').then(m => m.ConsultationModule),
        canActivate: [AuthGuard]
      },
      {
        path: 'clientele',
        loadChildren: () => import('src/app/features/clientele/clientele.module').then(m => m.ClienteleModule),
        canActivate: [AuthGuard]
      },
      {
        path: 'ressources',
        loadChildren: () => import('src/app/features/ressources/ressources.module').then(m => m.RessourcesModule),
        canActivate: [AuthGuard]
      },
      {
        path: 'reservation',
        loadChildren: () => import('src/app/features/reservation/reservation.module').then(m => m.ReservationModule),
        canActivate: [AuthGuard]
      },
      {
        path: 'facturation',
        loadChildren: () => import('src/app/features/facturation/facturation.module').then(m => m.FacturationModule),
        canActivate: [AuthGuard]
      },
      {
        path: 'notification',
        loadChildren: () => import('src/app/features/notification/notification.module').then(m => m.NotificationModule),
        canActivate: [AuthGuard]
      },
      {
        path: 'utilisateurs',
        loadChildren: () => import('src/app/features/utilisateurs/utilisateurs.module').then(m => m.UtilisateursModule),
        canActivate: [AuthGuard]
      }]
  },
  {
    path: 'error',
    component: ErrorComponent,
    loadChildren: () => import('src/app/shared/error/error.module').then(m => m.ErrorModule)
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'login',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
