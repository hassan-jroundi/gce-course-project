import { SessionService } from './session.service';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { CustomMenuItem } from '../models/menu-item.model';

@Injectable({
    providedIn: 'root',
})
/**
 * menu data service
 */
export class MenuDataService {

    public toggleMenuBar: BehaviorSubject<any> = new BehaviorSubject<any>(null);

    public sessionService: SessionService = new SessionService();

    getMenuList(): CustomMenuItem[] {
        let user = this.sessionService.getItem("currentUser");
        switch (user["profil"]["code"]) {
            case "ADM": {
                return [
                    {
                        Label: 'Accueil', Icon: 'fa-home', RouterLink: '/main/dashboard', Childs: null, IsChildVisible: false
                    },
                    {
                        Label: 'Consultation', Icon: 'fa-universal-access', RouterLink: null, Childs: [
                            { Label: 'Consultation Cheval', RouterLink: '/main/consultation/cheval', Childs: null, IsChildVisible: false },
                            { Label: 'Consultation Personne', RouterLink: '/main/consultation/personne', Childs: null, IsChildVisible: false }
                        ], IsChildVisible: false
                    },
                    {
                        Label: 'Clientèle', Icon: 'fa-address-book', RouterLink: null, Childs: [
                            { Label: 'Clientèle Cheval', RouterLink: '/main/clientele/cheval', Childs: null, IsChildVisible: false },
                            { Label: 'Clientèle Personne', RouterLink: '/main/clientele/personne', Childs: null, IsChildVisible: false }
                        ], IsChildVisible: false
                    },
                    {
                        Label: 'Réservation', Icon: 'fa-calendar', RouterLink: null, Childs: [
                            { Label: 'Réservation box', RouterLink: '/main/reservation/box', Childs: null, IsChildVisible: false },
                            { Label: 'Réservation lit', RouterLink: '/main/reservation/lit', Childs: null, IsChildVisible: false },
                            { Label: 'Réservation piste', RouterLink: '/main/reservation/piste', Childs: null, IsChildVisible: false }
                        ], IsChildVisible: false
                    },
                    {
                        Label: 'Ressources', Icon: 'fa-th-list', RouterLink: null, Childs: [
                            { Label: 'Bâtiment', RouterLink: "/main/ressources/immeuble", Childs: null, IsChildVisible: false },
                            { Label: 'Ecurie', RouterLink: "/main/ressources/ecurie", Childs: null, IsChildVisible: false },
                            { Label: 'Piste', RouterLink: "/main/ressources/piste", Childs: null, IsChildVisible: false }
                        ], IsChildVisible: false
                    },
                    {
                        Label: 'Facturation', Icon: 'fa-credit-card', RouterLink: '/main/facturation', Childs: [
                            { Label: 'Recherche par personne', RouterLink: "/main/facturation/personne", Childs: null, IsChildVisible: false },
                            { Label: 'Recherche par mois', RouterLink: "/main/facturation/mois", Childs: null, IsChildVisible: false }
                        ], IsChildVisible: false
                    },
                    {
                        Label: 'Gestion des utilisateurs', Icon: 'fa-user-circle', RouterLink: '/main/utilisateurs', Childs: null, IsChildVisible: false
                    }
                ];
            }
            case "GES": {
                return [
                    {
                        Label: 'Accueil', Icon: 'fa-home', RouterLink: '/main/dashboard', Childs: null, IsChildVisible: false
                    },
                    {
                        Label: 'Clientèle', Icon: 'fa-address-book', RouterLink: null, Childs: [
                            { Label: 'Clientèle Cheval', RouterLink: '/main/clientele/cheval', Childs: null, IsChildVisible: false },
                            { Label: 'Clientèle Personne', RouterLink: '/main/clientele/personne', Childs: null, IsChildVisible: false }
                        ], IsChildVisible: false
                    },
                    {
                        Label: 'Réservation', Icon: 'fa-calendar', RouterLink: null, Childs: [
                            { Label: 'Réservation box', RouterLink: '/main/reservation/box', Childs: null, IsChildVisible: false },
                            { Label: 'Réservation lit', RouterLink: '/main/reservation/lit', Childs: null, IsChildVisible: false },
                            { Label: 'Réservation piste', RouterLink: '/main/reservation/piste', Childs: null, IsChildVisible: false }
                        ], IsChildVisible: false
                    },
                    {
                        Label: 'Ressources', Icon: 'fa-th-list', RouterLink: null, Childs: [
                            { Label: 'Bâtiment', RouterLink: "/main/ressources/immeuble", Childs: null, IsChildVisible: false },
                            { Label: 'Ecurie', RouterLink: "/main/ressources/ecurie", Childs: null, IsChildVisible: false },
                            { Label: 'Piste', RouterLink: "/main/ressources/piste", Childs: null, IsChildVisible: false }
                        ], IsChildVisible: false
                    },
                    {
                        Label: 'Facturation', Icon: 'fa-credit-card', RouterLink: '/main/facturation', Childs: [
                            { Label: 'Recherche par personne', RouterLink: "/main/facturation/personne", Childs: null, IsChildVisible: false },
                            { Label: 'Recherche par mois', RouterLink: "/main/facturation/mois", Childs: null, IsChildVisible: false }
                        ], IsChildVisible: false
                    }
                ];
            }
            case "TEP": {
                return [
                    {
                        Label: 'Accueil', Icon: 'fa-home', RouterLink: '/main/dashboard', Childs: null, IsChildVisible: false
                    },
                    {
                        Label: 'Clientèle', Icon: 'fa-address-book', RouterLink: null, Childs: [
                            { Label: 'Clientèle Cheval', RouterLink: '/main/clientele/cheval', Childs: null, IsChildVisible: false },
                            { Label: 'Clientèle Personne', RouterLink: '/main/clientele/personne', Childs: null, IsChildVisible: false }
                        ], IsChildVisible: false
                    },
                    {
                        Label: 'Réservation', Icon: 'fa-calendar', RouterLink: null, Childs: [
                            { Label: 'Réservation box', RouterLink: '/main/reservation/box', Childs: null, IsChildVisible: false },
                            { Label: 'Réservation lit', RouterLink: '/main/reservation/lit', Childs: null, IsChildVisible: false },
                            { Label: 'Réservation piste', RouterLink: '/main/reservation/piste', Childs: null, IsChildVisible: false }
                        ], IsChildVisible: false
                    }
                ];
            }
            case "AGS": {
                return [
                    {
                        Label: 'Accueil', Icon: 'fa-home', RouterLink: '/main/dashboard', Childs: null, IsChildVisible: false
                    },
                    {
                        Label: 'Consultation', Icon: 'fa-universal-access', RouterLink: null, Childs: [
                            { Label: 'Consultation Cheval', RouterLink: '/main/consultation/cheval', Childs: null, IsChildVisible: false },
                            { Label: 'Consultation Personne', RouterLink: '/main/consultation/personne', Childs: null, IsChildVisible: false }
                        ], IsChildVisible: false
                    }
                ];
            }
            default: {
                return [
                    {
                        Label: 'Accueil', Icon: 'fa-home', RouterLink: '/main/dashboard', Childs: null, IsChildVisible: false
                    }
                ];
            }
          }
    }
}
