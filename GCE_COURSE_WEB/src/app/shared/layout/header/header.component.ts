import {HttpService} from './../../../core/services/http.service';
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {RouteStateService} from 'src/app/core/services/route-state.service';
import {SessionService} from 'src/app/core/services/session.service';
import {User} from 'src/app/core/models/user.model';
import {notification} from 'src/app/core/models/notification.model';
import {UserIdleService} from 'angular-user-idle';
import {ThemeService} from 'src/app/core/services/theme.service';
import {UserContextService} from 'src/app/core/services/user-context.service';
import {MenuDataService} from 'src/app/core/services/menu-data.service';
import {environment} from 'src/environments/environment';

let apiNotification = environment.apiNotification;
let apiReservation = environment.apiReservation;
let apiLogin = environment.apiLogin;

@Component({
  selector: 'app-header',
  templateUrl: 'header.component.html',
  styleUrls: ['header.component.css']
})
export class HeaderComponent implements OnInit {

  user: any;

  displayNotifications: boolean;
  notificationCount: any;

  notifications: notification[];
  notificationSelected: any;
  chevalReservations: any[];

  detailsNotificationDisplay: boolean = false;
  toutDisplay: boolean = false;

  nombreNotifsNonLues: number = 0;

  nomComplet: any = '';

  constructor(
    private router: Router,
    private routeStateService: RouteStateService,
    private sessionService: SessionService,
    private userIdle: UserIdleService,
    private themeService: ThemeService,
    private userContextService: UserContextService,
    private menuDataService: MenuDataService,
    private http: HttpService) {

    this.displayNotifications = false;
    // this.getNotifications();

    var selectedTheme = this.sessionService.getItem('selected-theme');
    if (selectedTheme) {
      this.selectTheme(selectedTheme);
    }
  }

  ngOnInit() {
    this.user = this.sessionService.getItem('currentUser');
    this.nomComplet = this.user["personne"]["nom"] + " " + this.user["personne"]["prenom"];
    this.notifications = [];
    // for (var i = 1; i <= 5; i++) {
    //   var notificationObj = new notification("Message " + i, new Date(), null)
    //   this.notifications.push(notificationObj);
    // }
    this.getNotifications();

    //Start watching for user inactivity.
    this.userIdle.startWatching();

    // Start watching when user idle is starting.
    this.userIdle.onTimerStart().subscribe();

    // Start watch when time is up.
    this.userIdle.onTimeout().subscribe(() => {
      this.logout();
    });

    setTimeout(() => {
      this.ngOnInit();
    }, 1000 * 120);
  }

  onClick(notification: any) {
    this.http.get(apiNotification + "dejaLu/notification/" + notification.id + "/utilisateur/" + this.sessionService.getItem("currentUser")["id"]).subscribe(res => {
      this.detailsNotificationDisplay = true;
      this.displayNotifications = false;
      this.notificationSelected = notification;
      this.getChevalReservations(notification.cheval);
      this.getNotifications();
    });

  }

  afficherTout() {
    this.displayNotifications = false;
    this.router.navigate(['/main/notification']);
    // this.detailsNotificationDisplay = false;
    // this.toutDisplay = true;
  }

  closeDetailsDialog() {
    this.detailsNotificationDisplay = false;
  }

  closeDialog() {
    this.toutDisplay = false;
  }

  deleteItem(chevalReservation: any) {

  }

  getChevalReservations(cheval: any) {
    this.http.get(apiReservation + 'encours/cheval/' + cheval['id']).subscribe((res: any) => {
      this.chevalReservations = res;
      for (let element of this.chevalReservations) {
        if (element['chevalBoxs'].length >= 1) {
          element['type'] = 'Hebergement Cheval';
        } else {
          element['type'] = 'Entrainement Cheval';
        }
      }
    });
  }

  getNotifications() {
    this.notifications = [];
    this.http.get(apiNotification + 'profil/partiel/' + this.sessionService.getItem("codeProfil") + "/utilisateur/" + this.sessionService.getItem("currentUser")["id"]).subscribe((res: any[]) => {
      let result = res;
      for (let element of result) {
        if (element["dejaLu"] == false) {
          this.nombreNotifsNonLues = this.nombreNotifsNonLues + 1;
        }
        let cheval = element['cheval'];
        var notificationObj = new notification(element["id"], element['description'], 'Sortie d\'entrainement', element['dateEnvoi'], element['type'], null, null, element["dejaLu"]);
        notificationObj.cheval = cheval;
        this.notifications.push(notificationObj);
      }
      this.notificationCount = this.notifications.length;
    });
    setTimeout(() => {
    }, 1600);

  }

  logout() {

    this.http.post(apiLogin + 'logout', this.sessionService.getItem('currentUser')['id'].toString()).subscribe(res => {
      this.userIdle.stopWatching();
      this.routeStateService.removeAll();
      this.userContextService.logout();
      this.sessionService.removeItem('active-menu');
      this.router.navigate(['/login']);
    });

    // console.info("current user", this.sessionService.getItem("currentUser"))
    // this.userIdle.stopWatching();
    // this.routeStateService.removeAll();
    // this.userContextService.logout();
    // this.sessionService.removeItem('active-menu');
    // this.router.navigate(['/login']);
  }

  showNotificationSidebar() {
    this.displayNotifications = true;
  }

  toggleMenu() {
    this.menuDataService.toggleMenuBar.next(true);
  }

  selectTheme(theme: string) {
    this.sessionService.setItem('selected-theme', theme);
    this.themeService.selectTheme(theme);
  }

}
