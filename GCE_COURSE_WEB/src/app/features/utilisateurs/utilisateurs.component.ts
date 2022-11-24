import {HttpService} from './../../core/services/http.service';
import {Component, OnInit} from '@angular/core';
import {environment} from 'src/environments/environment';
import {MessageService} from 'primeng/api';
import {ConfirmationService} from 'primeng';
import {UserCreationModificationService} from '../../core/services/user-creation-modification.service';
import {SessionService} from '../../core/services/session.service';

let apiUtilisateur = environment.apiUtilisateur;
let apiPersonne = environment.apiPersonne;
let apiProfil = environment.apiProfil;
let apiSite = environment.apiSite;

@Component({
  selector: 'app-utilisateurs',
  templateUrl: './utilisateurs.component.html'
})
export class UtilisateursComponent implements OnInit {

  utilisateurs: any[] = [];
  personnes: any[] = [];

  personne: any = undefined;
  utilisateur: any;

  display: boolean = false;
  selectionDisplay: boolean = false;
  detailsDisplay: boolean = false;
  passwordDisplay: boolean = false;

  action: any = '';

  typeUtilisateurList: any[];
  typeUtilisateur: any;

  etatList: any[];
  etat: any;

  nomPersonneSearch: any = '';
  prenomPersonneSearch: any = '';
  cinPersonneSearch: any = '';
  personneId: any = '';

  login: any = '';
  motDePasse: any = '';

  profilDropdown: any;
  profils: any[];
  profil: any;

  siteDropdown: any;
  sites: any[];
  site: any;

  idDetails: any = '';
  nomCompletDetails: any = '';
  loginDetails: any = '';
  typeUtilisateurDetails: any = '';

  motDePasseChange: any = '';


  constructor(private http: HttpService,
              private messageService: MessageService,
              private confirmationService: ConfirmationService,
              private userService: UserCreationModificationService,
              private sessionService: SessionService) {
    this.typeUtilisateurList = [
      {name: 'Interne', code: 'I'},
      {name: 'Externe', code: 'E'}
    ];
    this.etatList = [
      {name: 'Activé', code: 'A'},
      {name: 'Désactivé', code: 'D'}
    ]
  }

  ngOnInit() {
    this.loadUtilisateursList();
    this.loadProfilsList();
    this.loadSitesList();
  }

  addItem() {
    this.action = 'Ajouter';
    this.display = true;
  }

  loadUtilisateursList() {
    this.http.get(apiUtilisateur).subscribe((res: any[]) => {
      this.utilisateurs = res;
    });
  }

  loadProfilsList() {
    this.http.get(apiProfil).subscribe((res: any[]) => {
      this.profils = res;
    });
  }

  loadSitesList() {
    this.http.get(apiSite).subscribe((res: any[]) => {
      this.sites = res;
    });
  }

  onProfilChoosen(event) {
    if (event.value != null) {
      this.profil = event.value;
    }
  }

  onSiteChoosen(event) {
    if (event.value != null) {
      this.site = event.value;
    }
  }

  onTypeUtilisateurChoosen(event) {
    if (event.value != null) {
      if (event.value.code == 'I') {

      } else {

      }
    }
  }

  doPersonneSearch() {
    if (this.nomPersonneSearch.length == 0 && this.prenomPersonneSearch.length == 0 && this.cinPersonneSearch.length == 0) {
      this.messageService.add({severity: 'warn', summary: 'Attention', detail: 'Merci de saisir une valeur pour la recherche.'});
    } else {
      let params = {
        nom: this.nomPersonneSearch,
        prenom: this.prenomPersonneSearch,
        cin: this.cinPersonneSearch,
        designation: '',
        raisonSociale: '',
      };

      this.http.search(apiPersonne + 'search3', params).subscribe(
        (res: any[]) => {
          this.personnes = res;
          if (this.personnes.length == 1) {
            this.personne = this.personnes[0];
            this.nomPersonneSearch = this.personnes[0]['nom'] ? this.personnes[0]['nom'] : '';
            this.prenomPersonneSearch = this.personnes[0]['prenom'] ? this.personnes[0]['prenom'] : '';
            this.cinPersonneSearch = this.personnes[0]['numeroPieceIdentite'] ? this.personnes[0]['numeroPieceIdentite'] : '';
          } else if (this.personnes.length > 1) {
            this.selectionDisplay = true;
          } else {
            this.messageService.add({severity: 'error', summary: 'Erreur', detail: 'Aucune presonne trouvée ! '});
          }
        }
      );
    }
  }

  selectionner(personne: any) {
    this.personne = personne;
    this.nomPersonneSearch = personne.nom;
    this.prenomPersonneSearch = personne?.prenom;
    this.cinPersonneSearch = personne?.numeroPieceIdentite;
    this.personneId = personne?.id;

    this.selectionDisplay = false;
  }

  closeDialog() {
    this.display = false;
    this.resetSearchInputs();
  }

  closeAjouterDialog() {
    this.resetAjouterDialogInputs();
    this.display = false;
  }

  resetAjouterDialogInputs() {
    this.siteDropdown = '';
    this.profilDropdown = '';
    this.motDePasse = '';
    this.login = '';
    this.cinPersonneSearch = '';
    this.nomPersonneSearch = '';
    this.prenomPersonneSearch = '';
    this.personne = null;
    this.personneId = '';
  }

  closeSelectionDialog() {
    this.selectionDisplay = false;
  }

  resetSearchInputs() {
    this.nomPersonneSearch = '';
    this.prenomPersonneSearch = '';
    this.cinPersonneSearch = '';
  }

  checkAjouterDialogValidation(): boolean {
    // if (this.perso)
    return true;
  }

  saveItem() {
    if (this.action == 'Ajouter') {
      if (this.personne == undefined || this.login == "" || this.profilDropdown == undefined || this.siteDropdown == undefined) {
        this.messageService.add({severity: 'warn', summary: 'Attention', detail: 'Merci de remplir tous les champs nécessaires.'});
      } else {
        let utilisateur = {
          login: this.login,
          motDePasse: this.motDePasse,
          personne: this.personne,
          idProfil: this.profilDropdown.id,
          idSite: this.siteDropdown.id,
          codeTypeUtilisateur: this.typeUtilisateur.code,
          idSession: this.sessionService.getItem("currentUser")["idSession"]
        };
        this.http.post(apiUtilisateur, utilisateur).subscribe(res => {
          this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Utilisateur ajouté avec succès'});
          this.loadUtilisateursList();
          setTimeout(() => {
            this.resetAjouterDialogInputs();
            this.display = false;
          }, 1600);
        }, error => {
          this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Le login est déjà utilisé par un autre utilisateur."});
        });
      }
    } else {

    }
  }

  detailsItem(utilisateur: any) {
    this.utilisateur = utilisateur;
    this.detailsDisplay = true;
    this.idDetails = utilisateur.id;
    this.nomCompletDetails = utilisateur.personne.nom + " " + utilisateur.personne.prenom;
    this.loginDetails = utilisateur.login;
    this.typeUtilisateurDetails = utilisateur.codeTypeUtilisateur == 'I' ? 'Interne' : 'Externe';
    this.profilDropdown = this.profils.find(element => element.id == utilisateur.profil.id);
    this.siteDropdown = this.sites.find(element => element.id == utilisateur.site.id);
    this.etat = this.etatList.find(element => element.code == utilisateur.codeEtat);

  }

  deleteItem(utilisateur: any) {
    this.confirmationService.confirm({
      message: 'Etes-vous sûr de vouloir supprimer cet utilisateur ?',
      accept: () => {
        this.http.delete(apiUtilisateur, utilisateur["id"]).subscribe(res => {
          this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: "Utilisateur supprimé" });
          setTimeout(() => {
            this.loadUtilisateursList();
          }, 1500);
        }, error => {
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "L'utilisateur a déjà des actions effectuées et ne peut pas être supprimé." });
        });
      }
    });
  }

  closeDetailsDialog() {
    // this.resetDetailsInputs();
    this.detailsDisplay = false;
  }

  resetDetailsInputs() {
    this.idDetails = '';
    this.nomCompletDetails = '';
    this.loginDetails = '';
    this.typeUtilisateurDetails = '';
    this.profilDropdown = '';
    this.siteDropdown = '';
    this.etat = '';
  }

  updateItem() {
    let utilisateur = {
      id: this.utilisateur.id,
      login: this.loginDetails,
      codeEtat: this.etat.code,
      idProfil: this.profilDropdown.id,
      idSite: this.siteDropdown.id,
      idSession: this.sessionService.getItem("currentUser")["idSession"]
    };
    this.http.put(apiUtilisateur, utilisateur.id, utilisateur).subscribe(res => {
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: "Utilisateur modifié avec succès" });
      setTimeout(() => {
        this.detailsDisplay = false;
        this.loadUtilisateursList();
      }, 1600);
    }, error => {
      this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Le login est déjà utilisé par un autre utilisateur." });
    });
  }

  openPasswordDialog() {
    this.detailsDisplay = false;
    this.passwordDisplay = true;
  }

  closePasswordDialog() {
    this.motDePasseChange = '';
    this.passwordDisplay = false;
    this.detailsDisplay = true;
  }

  changerMotDePasse() {
    let params = {
      id: this.utilisateur.id,
      motDePasse: this.motDePasseChange,
      idSession: this.sessionService.getItem("currentUser")["idSession"]
    };
    this.http.search(apiUtilisateur + "changerMotDePasse", params).subscribe(res => {
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Mot de passe changé' });
      setTimeout(() => {
        this.passwordDisplay = false;
      }, 1200);
    });
  }

  resetPersonneChoisie() {
    this.personne = undefined;
    this.nomPersonneSearch = "";
    this.prenomPersonneSearch = "";
    this.cinPersonneSearch = "";
  }

}
