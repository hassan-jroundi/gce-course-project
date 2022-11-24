import { HttpService } from './../../core/services/http.service';
import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { notification } from 'src/app/core/models/notification.model';
import {SessionService} from '../../core/services/session.service';

let apiNotification = environment.apiNotification;
let apiReservation = environment.apiReservation;

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html'
})
export class NotificationComponent implements OnInit {

  notifications: notification[];
  notificationSelected: any;
  chevalReservations: any[];

  constructor(private http: HttpService, private sessionService: SessionService) {
    this.notifications = [];
  }

  ngOnInit() {
    this.getAllNotification();
  }

  getAllNotification() {
    this.http.get(apiNotification + "profil/" + this.sessionService.getItem("codeProfil") + "/utilisateur/" + this.sessionService.getItem("currentUser")["id"]).subscribe((res: any[]) => {
      let result = res;
      for (let element of result) {
        let cheval = element["cheval"];
        var notificationObj = new notification(element["id"], element["description"], "Sortie d'entrainement", element["dateEnvoi"], element["type"], null, null, element["dejaLu"]);
        notificationObj.cheval = cheval;
        this.notifications.push(notificationObj);
      }
    });
    setTimeout(() => {
    }, 1600);
  }

  change(event) {
    this.notificationSelected = event.value;
    this.http.get(apiNotification + "dejaLu/notification/" + this.notificationSelected[0]["id"] + "/utilisateur/" + this.sessionService.getItem("currentUser")["id"]).subscribe(res => {
      this.getChevalReservations(this.notificationSelected[0].cheval);
    });
  }

  getChevalReservations(cheval: any) {
    this.http.get(apiReservation + "encours/cheval/" + cheval["id"]).subscribe((res: any) => {
      this.chevalReservations = res;
      for (let element of this.chevalReservations) {
        if (element["chevalBoxs"].length >= 1) {
          element["type"] = "Hebergement Cheval";
        } else {
          element["type"] = "Entrainement Cheval";
        }
      }
    });
  }

  deleteItem(chevalReservation: any) {

  }

}
