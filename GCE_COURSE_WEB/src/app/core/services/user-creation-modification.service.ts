import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {HttpService} from './http.service';
import {environment} from '../../../environments/environment';
import {SessionService} from './session.service';

let apiBase = environment.apiBase;

@Injectable({
  providedIn: 'root',
})
/**
 * loader service
 * toggle loader gif in website
 */
export class UserCreationModificationService {


  constructor(private http: HttpService, private sessionService: SessionService) {
  }

  creation(object: any) {
    this.http.put(apiBase + 'creation', this.sessionService.getItem('currentUser')['idSession'], object).subscribe(res => {
    });
  }

  modification(object: any) {
    this.http.put(apiBase + 'modification', this.sessionService.getItem('currentUser')['idSession'], object).subscribe(res => {
    });
  }
}
