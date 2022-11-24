import { element } from 'protractor';
import { TypePersonne } from './../../../core/models/type-personne';
import { MessageService } from 'primeng/api';
import { ConfirmationService } from 'primeng';
import { Personne } from './../../../core/models/personne';
import { HttpService } from './../../../core/services/http.service';
import { Component, OnInit } from '@angular/core';
import { RaisonSociale } from 'src/app/core/models/raison-sociale.model';
import { environment } from 'src/environments/environment';
import { ifStmt } from '@angular/compiler/src/output/output_ast';
import { DatePipe } from '@angular/common';
import {SessionService} from '../../../core/services/session.service';

let apiPersonneM = environment.apiPersonneM;
let apiPersonne = environment.apiPersonne;
let apiReservation = environment.apiReservation;
let apiRelationLAD = environment.apiRelationLAD;

@Component({
  selector: 'app-clientele-personne',
  templateUrl: './clientele-personne.component.html'
})
export class ClientelePersonneComponent implements OnInit {

  profilUtilisateurConnecte: any;

  personnes: any[];
  modifierEmployeurPersonnes: any[] = [];
  personneList: any[];
  raisonSocialeList: RaisonSociale[];
  raisonSocialeAjouterList: RaisonSociale[];
  raisonSocialeModifierEmployeurList: RaisonSociale[];
  typePersonneList: TypePersonne[];
  raisonSocialeCatched: RaisonSociale;
  raisonSocialeAjouterCatched: RaisonSociale;
  raisonSocialeModifierEmployeurCatched: RaisonSociale;

  //Search filter
  nomSearch: any = '';
  prenomSearch: any = '';
  cinSearch: any = '';
  raisonSocialeSearch: any = '';
  designationSearch: any = '';
  typeRaisonSocialeSearch: any;
  typeRaisonSocialeAjouter: any;

  //Boolean values
  loading: boolean = false;
  addLoading: boolean = false;
  ajouterDisplay: boolean = false;
  detailsDisplay: boolean = false;
  tableDisplay: boolean = false;
  listeSearchDisplay: boolean = false;
  historiqueReservationsDisplay: boolean = false;
  savePersonne: boolean = false;
  modifierEmployeurDisplay: boolean = false;

  //Add-Edit dialog values
  id: any = '';
  nom: any = '';
  prenom: any;
  cin: any = '';
  designation: any = '';
  nomSociete: any = '';
  raisonSociale: any;
  type: any;
  dateNaissance: Date = new Date("2002-01-01");
  numeroTelephone: any;
  adresseMail: any;
  adresse: any;
  nomGerant: any;
  nomEmployeur: any = '';
  prenomEmployeur: any = '';
  cinEmployeur: any = '';
  designationEmployeur: any = '';
  raisonSocialeEmployeur: any = '';
  idEmployeur: any = '';
  employeur: any;

  //Details dialog values
  idDetails: any = '';
  nomDetails: any = '';
  prenomDetails: any;
  cinDetails: any = '';
  designationDetails: any = '';
  nomSocieteDetails: any = '';
  raisonSocialeDetails: any;
  typeDetails: any;
  dateNaissanceDetails: Date = new Date();
  numeroTelephoneDetails: any;
  adresseMailDetails: any;
  adresseDetails: any;
  nomGerantDetails: any;
  codeDatabaseSourceDetails: any;
  typePersonneDetails: TypePersonne = new TypePersonne();
  typePersonne: TypePersonne = new TypePersonne();
  nomEmployeurDetails: any = '';
  prenomEmployeurDetails: any = '';
  cinEmployeurDetails: any = '';
  designationEmployeurDetails: any = '';
  raisonSocialeEmployeurDetails: any = '';
  idEmployeurDetails: any = '';
  employeurDetails: any = undefined;

  //Modifier employeur
  nomModifierEmployeur: any = '';
  prenomModifierEmployeur: any = '';
  cinModifierEmployeur: any = '';
  designationModifierEmployeur: any = '';
  raisonSocialeModifierEmployeur: any = '';
  idModifierEmployeur: any = '';

  action: any;
  reservations: any[];

  choixRaisonSociale: any;
  choixRaisonSocialeAjouter: any;
  choixRaisonSocialeModifierEmployeur: any;
  codeDatabaseSource: any;

  constructor(private http: HttpService, private confirmationService: ConfirmationService, private messageService: MessageService, private sessionService: SessionService) {
    this.profilUtilisateurConnecte = this.sessionService.getItem("codeProfil");
    this.raisonSocialeList = [
      { name: 'Personne Physique', code: 'P' },
      { name: 'Personne Morale', code: 'M' },
      { name: 'Association', code: 'A' }
    ];
    this.raisonSocialeAjouterList = [
      { name: 'Personne Physique', code: 'P' },
      { name: 'Personne Morale', code: 'M' },
      { name: 'Association', code: 'A' }
    ];
    this.raisonSocialeModifierEmployeurList = [
      { name: 'Personne Physique', code: 'P' },
      { name: 'Personne Morale', code: 'M' },
      { name: 'Association', code: 'A' }
    ];
    this.typePersonneList = [
      { name: 'LAD', code: 'L' },
      { name: 'Personnel', code: 'P'}
    ];
  }

  ngOnInit() {
  }

  doSearch() {
    this.tableDisplay = false;
    if (this.nomSearch.length == 0 && this.prenomSearch.length == 0 && this.cinSearch.length == 0 && this.designationSearch.length == 0 && this.raisonSocialeSearch.length == 0) {
      this.messageService.add({ severity: 'warn', summary: 'Attention', detail: 'Merci de saisir une valeur pour la recherche.' });
    } else {
      this.loading = true;
      let params = {
        nom: this.nomSearch,
        prenom: this.prenomSearch,
        cin: this.cinSearch,
        designation: this.designationSearch,
        raisonSociale: this.raisonSocialeSearch
      }

      this.http.search(apiPersonne + 'search2', params).subscribe(
        (res: any[]) => {

          this.personneList = res;
          // this.personneList = this.personneList.filter(element => element.databaseSource == "G");
          if (this.personneList.length == 0) {
            this.loading = false;
            this.messageService.add({ severity: 'warn', summary: 'Attention', detail: 'Aucune personne trouvée.' });
          } else {
            this.tableDisplay = true;
            this.loading = false;
          }
        }
      );
      // setTimeout(() => {
      //   this.tableDisplay = true;
      //   this.loading = false;adre
      // }, 1600);
    }
  }

  loadPersonneList() {

  }

  historiqueReservationItem(personne: Personne) {
    this.http.get(apiReservation + "personne/" + personne["id"]).subscribe(
      (res: any[]) => {
        this.reservations = res;
        for (let element of this.reservations) {
          element["type"] = "Hebergement Personne";
        }
      }
    );
    this.historiqueReservationsDisplay = true;
  }

  closeHistoriqueReservationsDialog() {
    this.reservations = [];
    this.historiqueReservationsDisplay = false;
  }

  addItem() {
    this.reset();
    this.resetNonSearchInputs();
    this.action = "Ajouter";
    this.ajouterDisplay = true;
    this.addLoading = false;
    this.typePersonne = this.typePersonneList[0];
  }

  detailsItem(personne: Personne) {
    this.action = "Details";
    if (personne?.codeTypePersonne == "L") {
      this.http.get(apiPersonne + "employeur/" + personne["id"]).subscribe(res => {
        if (res["codeNaturePersonne"] == "P") {
          this.nomEmployeurDetails = res["nom"];
          this.prenomEmployeurDetails = res["prenom"];
          this.cinEmployeurDetails = res["numeroPieceIdentite"];
        }
        if (res["codeNaturePersonne"] == "M") {
          this.raisonSocialeEmployeurDetails = res["raisonSociale"];
        }
        if (res["codeNaturePersonne"] == "A") {
          this.designationEmployeurDetails = res["designation"];
        }
      });
    }
    this.codeDatabaseSource = personne.databaseSource;
    this.codeDatabaseSourceDetails = personne.databaseSource;
    this.nomDetails = personne?.nom;
    this.prenomDetails = personne?.prenom;
    this.cinDetails = personne?.numeroPieceIdentite;
    this.designationDetails = personne?.designation;
    this.raisonSocialeDetails = personne?.raisonSociale;
    this.numeroTelephoneDetails = personne?.numeroTelephone1;
    this.adresseMailDetails = personne?.email;
    this.adresseDetails = personne?.adresse1;
    this.nomGerantDetails = personne?.nomGerant;
    this.dateNaissanceDetails = personne?.dateNaissance;
    this.idDetails = personne?.id;
    if (personne?.codeTypePersonne == 'L') {
      this.typePersonneDetails = this.typePersonneList[0];
    }
    if (personne?.codeTypePersonne == 'P') {
      this.typePersonneDetails = this.typePersonneList[1];
    }

    this.detailsDisplay = true;
  }

  updateItem() {
    let personne = {
      id: this.idDetails,
      nom: this.nomDetails,
      prenom: this.prenomDetails,
      numeroPieceIdentite: this.cinDetails,
      designation: this.designationDetails,
      raisonSociale: this.raisonSocialeDetails,
      dateNaissance: this.dateNaissanceDetails,
      numeroTelephone1: this.numeroTelephoneDetails,
      adresse1: this.adresseDetails,
      email: this.adresseMailDetails,
      nomGerant: this.nomGerantDetails,
      codeNaturePersonne: "P",
      codeTypePersonne: this.typePersonneDetails.code,
      idEmployeur: this.idModifierEmployeur,
      idSession: this.sessionService.getItem("currentUser")["idSession"]
    };

    this.http.put(apiPersonne, this.idDetails, personne).subscribe(res => {
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Modification effectuée.' });
      setTimeout(() => {
        this.personneList = [];
        this.tableDisplay = false;
        this.detailsDisplay = false;
        this.resetDetailsInputs();
        this.resetSearchInpus();
      }, 900);
    }, error => {
      this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Personne non trouvée sur la base de données." });
    });
  }

  saveItem() {
    if (this.nom == "" || this.prenom == "" || this.cin == "" || (this.typePersonne.code == "L" && this.employeur == undefined)) {
      this.messageService.add({ severity: 'warn', summary: 'Attention', detail: "Merci de saisir les champs nécessaires." });
    } else {
      let personne = {
        id: this.id,
        nom: this.nom,
        prenom: this.prenom,
        numeroPieceIdentite: this.cin,
        designation: this.designation,
        raisonSociale: this.raisonSociale,
        dateNaissance: this.dateNaissance,
        numeroTelephone1: this.numeroTelephone,
        adresse1: this.adresse,
        email: this.adresseMail,
        codeNaturePersonne: "P",
        codeTypePersonne: this.typePersonne.code,
        idEmployeur: this.idEmployeur,
        idSession: this.sessionService.getItem("currentUser")["idSession"]
      }

      this.http.post(apiPersonne, personne).subscribe(
        res => {
          this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Nouvelle personne ajoutée.' });
          setTimeout(() => {
            this.ajouterDisplay = false;
            this.resetEmployeurChoisi();
            this.resetSearchInpus();
            this.resetNonSearchInputs();
          }, 2000);
        }, error => {
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Personne déjà existante sur la base." });
        }
      );
    }
  }

  onRaisonSocialeChange(event) {
    this.typeRaisonSocialeAjouter = event.value;
    this.tableDisplay = false;
    this.raisonSocialeCatched = event.value;
    this.choixRaisonSociale = this.raisonSocialeCatched?.code;
    this.cin = '';
    this.raisonSociale = '';
    this.designation = '';
    this.resetNonSearchInputs();
    this.resetSearchInpus();
  }

  onRaisonSocialeAjouterChange(event) {
    // this.tableDisplay = false;
    this.raisonSocialeAjouterCatched = event.value;
    this.choixRaisonSocialeAjouter = this.raisonSocialeAjouterCatched?.code;
    this.nomEmployeur = '';
    this.prenomEmployeur = '';
    this.cinEmployeur = '';
    this.raisonSocialeEmployeur = '';
    this.designationEmployeur = '';
  }

  onRaisonSocialeModifierEmployeurChange(event) {
    this.raisonSocialeModifierEmployeurCatched = event.value;
    this.choixRaisonSocialeModifierEmployeur = this.raisonSocialeModifierEmployeurCatched?.code;
    this.nomModifierEmployeur = '';
    this.prenomModifierEmployeur = '';
    this.cinModifierEmployeur = '';
    this.raisonSocialeModifierEmployeur = '';
    this.designationModifierEmployeur = '';
  }

  doModifierEmployeurSearch() {
    this.modifierEmployeurPersonnes = [];
    if (this.nomModifierEmployeur.length == 0 && this.prenomModifierEmployeur.length == 0 && this.cinModifierEmployeur.length == 0 && this.designationModifierEmployeur.length == 0 && this.raisonSocialeModifierEmployeur.length == 0) {
      this.messageService.add({ severity: 'warn', summary: 'Attention', detail: 'Merci de saisir une valeur pour la recherche.' });
    } else {
      let params = {
        nom: this.nomModifierEmployeur,
        prenom: this.prenomModifierEmployeur,
        cin: this.cinModifierEmployeur,
        designation: this.designationModifierEmployeur,
        raisonSociale: this.raisonSocialeModifierEmployeur
      }

      this.http.search(apiPersonne + 'search2', params).subscribe(
        (res: any[]) => {
          for (let element of res) {
            if (element["codeTypePersonne"] != "L") {
              this.modifierEmployeurPersonnes.push(element);
            }
          }
        }
      );
    }
  }

  onTypePersonneChange(event) {
    this.nomEmployeur = '';
    this.prenomEmployeur = '';
    this.cinEmployeur = '';
    this.designationEmployeur = '';
    this.raisonSocialeEmployeur = '';
    this.typePersonne = event.value;
  }

  deleteItem(personne: any) {
    this.confirmationService.confirm({
      message: 'Etes-vous sûr de vouloir supprimer cette personne ?',
      accept: () => {
        this.http.delete(apiPersonne, personne["id"]).subscribe(res => {
          this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: "Personne supprimée" });
          setTimeout(() => {
            this.resetSearchInpus();
            this.tableDisplay = false;
            this.personneList = [];
          }, 1500);
        }, error => {
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Il n'est pas possible de supprimer une personne liée à des actions ou des réservations." });
        });
      }
    });
  }

  reset() {
    this.typeRaisonSocialeSearch = null;
    this.designation = '';
    this.raisonSociale = '';
    this.cin = '';
    this.choixRaisonSociale = '';
    this.addLoading = false;
    this.savePersonne = false;
  }

  resetNonSearchInputs() {
    this.nom = '';
    this.prenom = '';
    this.adresse = '';
    this.adresseMail = '';
    this.numeroTelephone = '';
    this.nomGerant = '';
    this.dateNaissance = new Date("2002-01-01");
    this.nomEmployeur = '';
    this.prenomEmployeur = '';
    this.cinEmployeur = '';
    this.designationEmployeur = '';
    this.raisonSocialeEmployeur = '';
    this.typePersonne = new TypePersonne();
  }

  resetSearchInpus() {
    this.nomSearch = '';
    this.prenomSearch = '';
    this.raisonSocialeSearch = '';
    this.designationSearch = '';
    this.cinSearch = '';
  }

  doSelectionner() {

  }

  onTest(event) {
  }

  onKeyupEnter() {

    let pipe = new DatePipe('en-US');
    this.resetNonSearchInputs();
    this.addLoading = true;
    let params = {
      numeroPieceIdentite: this.cin != '' ? this.cin : '',
      raisonSociale: this.raisonSociale != '' ? this.raisonSociale : '',
      designation: this.designation != '' ? this.designation : '',
      codeNaturePersonne: this.raisonSocialeCatched?.code
    }

    this.http.search(apiPersonneM + 'search', params).subscribe(
      (res: any[]) => {
        this.personnes = res;

        if (this.personnes.length == 1) {
          this.nom = this.personnes[0]["nom"] != '' ? this.personnes[0]["nom"] : '';
          this.prenom = this.personnes[0]["prenom"] != '' ? this.personnes[0]["prenom"] : '';
          this.dateNaissance = this.personnes[0]["dateNaissance"] != null ? this.personnes[0]["dateNaissance"] : null;
          this.numeroTelephone = this.personnes[0]["numeroTelephone1"] != '' ? this.personnes[0]["numeroTelephone1"] : '';
          this.adresseMail = this.personnes[0]["email"] != '' ? this.personnes[0]["email"] : '';
          this.adresse = this.personnes[0]["adresse1"] != '' ? this.personnes[0]["adresse1"] : '';
          this.id = this.personnes[0]["id"];
          this.cin = this.personnes[0]["numeroPieceIdentite"];
          this.raisonSociale = this.personnes[0]["raisonSociale"];
          this.nomGerant = this.personnes[0]["nomGerant"];
          this.addLoading = false;
          this.savePersonne = true;

        } else if (this.personnes.length > 1) {
          this.listeSearchDisplay = true;
        } else {
          this.addLoading = false;
          this.messageService.add({ severity: 'warn', summary: 'Attention', detail: 'Aucune personne trouvée' });
        }
      }
    );
  }

  doEmployeurAjouterSearch() {
    let params = {
      numeroPieceIdentite: this.cinEmployeur != '' ? this.cinEmployeur : '',
      nom: this.nomEmployeur != '' ? this.nomEmployeur : '',
      prenom: this.prenomEmployeur != '' ? this.prenomEmployeur : '',
      designation: this.designationEmployeur != '' ? this.designationEmployeur : '',
      raisonSociale: this.raisonSocialeEmployeur != '' ? this.raisonSocialeEmployeur : ''
    }

    this.http.search(apiPersonneM + 'search2', params).subscribe(
      (res: any[]) => {
        if (res.length == 1) {
          this.cinEmployeur = res[0]["numeroPieceIdentite"];
          this.nomEmployeur = res[0]["nom"];
          this.prenomEmployeur = res[0]["prenom"];
          this.designationEmployeur = res[0]["designation"];
          this.raisonSocialeEmployeur = res[0]["raisonSociale"];
          this.idEmployeur = res[0]["id"];
          this.employeur = res[0];

        }
        if (res.length > 1) {
          this.personnes = res;
          this.listeSearchDisplay = true;
        }
        if (res.length == 0) {
          this.messageService.add({ severity: 'warn', summary: 'Attention', detail: 'Aucune personne trouvée' });
          this.cinEmployeur = "";
          this.nomEmployeur = "";
          this.prenomEmployeur = "";
          this.idEmployeur = "";
          this.designationEmployeur = "";
          this.raisonSocialeEmployeur = "";
        }
      }
    );
  }

  doEmployeurDetailsSearch() {
    let params = {
      numeroPieceIdentite: this.cinEmployeurDetails != undefined ? this.cinEmployeurDetails : '',
      nom: this.nomEmployeurDetails != undefined ? this.nomEmployeurDetails : '',
      prenom: this.prenomEmployeurDetails != undefined ? this.prenomEmployeurDetails : ''
    }

    this.http.search(apiPersonneM + 'search2', params).subscribe(
      (res: any[]) => {
        if (res.length == 1) {
          this.cinEmployeurDetails = res[0]["numeroPieceIdentite"];
          this.nomEmployeurDetails = res[0]["nom"];
          this.prenomEmployeurDetails = res[0]["prenom"];
          this.designationEmployeurDetails = res[0]["designation"];
          this.raisonSocialeEmployeurDetails = res[0]["raisonSociale"];
          this.idEmployeurDetails = res[0]["id"];
          this.employeurDetails = res[0];
        }
        if (res.length > 1) {
          this.personnes = res;
          this.listeSearchDisplay = true;
        }
        if (res.length == 0) {
          this.messageService.add({ severity: 'warn', summary: 'Attention', detail: 'Aucune personne trouvée' });
          this.cinEmployeurDetails = "";
          this.nomEmployeurDetails = "";
          this.prenomEmployeurDetails = "";
          this.designationEmployeurDetails = "";
          this.raisonSocialeEmployeurDetails = "";
          this.idEmployeurDetails = "";
        }
      }
    );
  }

  modifierEmployeurSelectionner(personne: Personne) {
    this.idModifierEmployeur = personne?.id;
    if (personne.codeNaturePersonne == "P") {
      this.nomEmployeurDetails = personne?.nom;
      this.prenomEmployeurDetails = personne?.prenom;
      this.cinEmployeurDetails = personne?.numeroPieceIdentite;
      this.designationEmployeurDetails = '';
      this.raisonSocialeEmployeurDetails = '';
    }
    if (personne.codeNaturePersonne == "M") {
      this.nomEmployeurDetails = '';
      this.prenomEmployeurDetails = '';
      this.cinEmployeurDetails = '';
      this.designationEmployeurDetails =  '';
      this.raisonSocialeEmployeurDetails = personne?.raisonSociale;
    }
    if (personne.codeNaturePersonne == "A") {
      this.nomEmployeurDetails = '';
      this.prenomEmployeurDetails = '';
      this.cinEmployeurDetails = '';
      this.designationEmployeurDetails = personne?.designation;
      this.raisonSocialeEmployeurDetails = '';
    }
    this.modifierEmployeurDisplay = false;
  }

  selectionner(personne: Personne) {

    if (this.action == "Ajouter") {
      this.nomEmployeur = personne?.nom;
      this.prenomEmployeur = personne?.prenom;
      this.cinEmployeur = personne?.numeroPieceIdentite;
      this.designationEmployeur = personne?.designation;
      this.raisonSocialeEmployeur = personne?.raisonSociale;
      this.idEmployeur = personne?.id;
      this.employeurDetails = personne;
      this.employeur = personne;
    }

    if (this.action == "Details") {
      this.nomEmployeurDetails = personne?.nom;
      this.prenomEmployeurDetails = personne?.prenom;
      this.cinEmployeurDetails = personne?.numeroPieceIdentite;
      this.designationEmployeurDetails = personne?.designation;
      this.raisonSocialeEmployeurDetails = personne?.raisonSociale;
      this.idEmployeurDetails = personne?.id;
      this.employeurDetails = personne;
      this.employeur = personne;
    }

    // this.nom = personne?.nom;
    // this.prenom = personne?.prenom;
    // this.dateNaissance = personne?.dateNaissance;
    // this.numeroTelephone = personne?.numeroTelephone1;
    // this.adresseMail = personne?.email;
    // this.adresse = personne?.adresse1;
    // this.id = personne?.id;
    // this.cin = personne?.numeroPieceIdentite;
    // this.nomGerant = personne?.nomGerant;
    // this.designation = personne?.designation;
    // this.raisonSociale = personne?.raisonSociale;
    // this.savePersonne = true;

    this.listeSearchDisplay = false;
  }

  closeSelectionDialog() {
    this.addLoading = false;
    this.listeSearchDisplay = false;
  }

  closeModifierEmployeurDialog() {
    this.nomModifierEmployeur = '';
    this.prenomModifierEmployeur = '';
    this.cinModifierEmployeur = '';
    this.designationModifierEmployeur = '';
    this.raisonSocialeModifierEmployeur = '';
    this.modifierEmployeurPersonnes = [];
    this.modifierEmployeurDisplay = false;
  }

  openModifierEmployeurDialog() {
    this.modifierEmployeurDisplay = true;
  }

  modifierEmployeur() {

  }

  closeAjouterDialog() {
    this.resetNonSearchInputs();
    this.ajouterDisplay = false;
  }

  closeDetailsDialog() {
    this.detailsDisplay = false;
    this.resetDetailsInputs();
  }

  resetDetailsInputs() {
    this.action = "";
    this.codeDatabaseSource = "";
    this.codeDatabaseSourceDetails = "";
    this.nomDetails = "";
    this.prenomDetails = "";
    this.cinDetails = "";
    this.designationDetails = "";
    this.raisonSocialeDetails = "";
    this.numeroTelephoneDetails = "";
    this.adresseMailDetails = "";
    this.adresseDetails = "";
    this.nomGerantDetails = "";
    this.idDetails = "";
    this.typePersonneDetails = new TypePersonne();
    this.nomEmployeurDetails = "";
    this.prenomEmployeurDetails = "";
    this.cinEmployeurDetails = "";
    this.designationEmployeurDetails = "";
    this.raisonSocialeEmployeurDetails = "";

  }

  resetEmployeurChoisi() {
    this.employeur = undefined;
    this.nomEmployeur = "";
    this.prenomEmployeur = "";
    this.cinEmployeur = "";
    this.designationEmployeur = "";
    this.raisonSocialeEmployeur = "";
  }

}
