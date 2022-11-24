import {SiteService} from './../../core/services/site.service';
import {HttpService} from './../../core/services/http.service';
import {Component, OnInit} from '@angular/core';
import {UserDataService} from 'src/app/core/services/user-data.service';
import {RouteStateService} from 'src/app/core/services/route-state.service';
import {SessionService} from 'src/app/core/services/session.service';
import {TranslateService} from '@ngx-translate/core';
import {UserContextService} from 'src/app/core/services/user-context.service';
import {environment} from 'src/environments/environment';
import {MessageService} from 'primeng/api';

let apiLogin = environment.apiLogin;

@Component({
  selector: 'app-login',
  templateUrl: 'login.component.html',
  styleUrls: ['login.component.css']
})
export class LoginComponent implements OnInit {

  userName: string;

  password: string;

  locale: string;

  version: string;

  sitesResult: any = [];

  site: any;

  utilisateur: any;

  constructor(
    private userService: UserDataService,
    private messageService: MessageService,
    private routeStateService: RouteStateService,
    private sessionService: SessionService,
    public translate: TranslateService,
    private userContextService: UserContextService,
    private http: HttpService,
    private siteService: SiteService
  ) { }

  ngOnInit() {
    this.getAllPistes();
    this.userName = "";
    this.password = "";
    this.locale = this.sessionService.getItem("ng-prime-language");
    this.version = environment.version;
  }

  getAllPistes() {
    this.http
      .get(environment.apiSite)
      .subscribe((result) => {
        this.sitesResult = result;
        let emptySite = {
          "adresse": "",
          "id": "",
          "nom": "",
          "ville": ""
        };
        this.sitesResult.splice(0, 0, emptySite);
      });
  }

  onClickLogin() {
    let utilisateur = {
      login: this.userName,
      motDePasse: this.password
    };
    this.http.post(apiLogin, utilisateur).subscribe(res => {
      this.utilisateur = res;
      this.userContextService.setUser(this.utilisateur);
      this.routeStateService.add("Dashboard", '/main/dashboard', null, true);
      this.siteService.site = this.utilisateur.site;
      this.sessionService.setItem("site", this.utilisateur.site);
      this.sessionService.setItem("codeProfil", this.utilisateur.profil.code);
      return;
    }, error => {
      this.messageService.add({ severity: 'error', detail: "Nom d'utilisateur ou mot de passe incorrect" });
      return;
    });
    // let user: User = this.userService.getUserByUserNameAndPassword(this.userName, this.password);
    // if (user) {
    //   this.userContextService.setUser(user);
    //   this.routeStateService.add("Dashboard", '/main/dashboard', null, true);
    //   this.siteService.site = this.site;
    //   this.sessionService.setItem("site", this.site);
    //   return;
    // }
    // this.toastService.addSingle('error', '', 'Invalid user.');
    // return;
  }

  onLanguageChange($event) {
    this.locale = $event.target.value;
    if (this.locale == undefined || this.locale == null || this.locale.length == 0) {
      this.locale = "en";
    }
    // the lang to use, if the lang isn't available, it will use the current loader to get them
    this.translate.use(this.locale);
    this.sessionService.setItem("ng-prime-language", this.locale);
  }

  getUrl() {
    return "url('assets/images/bg-home.png')";
  }

  doNothing() {
    console.info("do nothing");
  }

}
