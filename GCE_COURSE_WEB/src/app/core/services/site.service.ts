import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
    providedIn: 'root',
})
/**
 * loader service
 * toggle loader gif in website
 */
export class SiteService {
    public site: any;
}