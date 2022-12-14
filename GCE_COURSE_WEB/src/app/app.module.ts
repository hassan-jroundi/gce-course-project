import { NotificationModule } from './features/notification/notification.module';
import { ReservationService } from './core/services/reservation-service';
import { AppMaterialModule } from './app.material.module';
import { SchedulerModule } from './shared/scheduler/scheduler.module';
import { ClienteleModule } from './features/clientele/clientele.module';
import { RessourcesModule } from './features/ressources/ressources.module';
import { FacturationModule } from './features/facturation/facturation.module';
import { ReservationModule } from './features/reservation/reservation.module';
import { ConsultationModule } from './features/consultation/consultation.module';
// angular default
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// Prime NG
import { MessageService } from 'primeng/api';
// app related
import { AppComponent } from 'src/app/app.component';
import { AuthGuard } from 'src/app/core/gaurds/auth.gaurd';
import { AppRoutingModule } from 'src/app/app.routing.module';
import { LayoutComponent } from 'src/app/shared/layout/layout.component';
import { MenuComponent } from 'src/app/shared/layout/menu/menu.component';
import { HeaderComponent } from 'src/app/shared/layout/header/header.component';
import { FooterComponent } from 'src/app/shared/layout/footer/footer.component';
import { UserIdleModule } from 'angular-user-idle';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { AppCommonModule } from 'src/app/app.common.module';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [
    AppComponent,
    LayoutComponent,
    MenuComponent,
    HeaderComponent,
    FooterComponent
  ],
  imports: [
    ConsultationModule,
    ReservationModule,
    FacturationModule,
    NotificationModule,
    RessourcesModule,
    ClienteleModule,
    SchedulerModule,
    AppMaterialModule,
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    UserIdleModule.forRoot({ idle: 300, timeout: 1, ping: null }),
    HttpClientModule,
    AppCommonModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  exports: [TranslateModule],
  providers: [
    MessageService,
    ReservationService,
    AuthGuard
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }
