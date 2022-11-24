import { ConfirmationService, MessageService } from 'primeng';
import { element } from 'protractor';
import { Cheval } from './../../../core/models/cheval';
import { HttpService } from './../../../core/services/http.service';
import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { DatePipe } from '@angular/common';
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';
import {SessionService} from '../../../core/services/session.service';

let apiStatutChevalM = environment.apiStatutChevalM;
let apiReservation = environment.apiReservation;
let apiCheval = environment.apiCheval;

@Component({
  selector: 'app-clientele-cheval',
  templateUrl: './clientele-cheval.component.html'
})
export class ClienteleChevalComponent implements OnInit {

  chevals: any[];
  chevalList: any[];

  id: any;
  numeroEsrima: any;
  numeroEsrimaSearch: any = "";
  numeroTranspondeurSearch: any = "";
  numeroTranspondeur: any;
  nom: any;
  nomSearch: any = "";
  proprioSearch: any = "";
  code: any;
  etat: any = "";
  dateEtat: any;
  dateEtatDate: Date;
  action: any;
  reservations: any[];

  nomEntraineur: any;
  prenomEntraineur: any;
  dateDebutEntraineur: any;
  nomProprietaire: any;
  prenomProprietaire: any;
  dateDebutProprietaire: any;

  display: boolean = false;
  listeSearchDisplay: boolean = false;
  tableDisplay: boolean = false;
  loading: boolean = false;
  addLoading: boolean = false;
  historiqueReservationsDisplay: boolean = false;

  profilUtilisateurConnecte: any;

  constructor(private http: HttpService, private confirmationService: ConfirmationService, private messageService: MessageService, private sessionService: SessionService) {
    this.profilUtilisateurConnecte = this.sessionService.getItem("codeProfil");
  }

  ngOnInit() {
  }

  doSearch() {
    this.tableDisplay = false;
    this.chevalList = [];
    this.loading = true;
    let params = {
      numeroEsrima: this.numeroEsrimaSearch != '' ? this.numeroEsrimaSearch : '',
      nomCheval: this.nomSearch != '' ? this.nomSearch : '',
      numeroTranspondeur: this.numeroTranspondeurSearch != '' ? this.numeroTranspondeurSearch : '',
      nomProprietaire: this.proprioSearch
    }

    this.http.search(environment.apiCheval + "search", params).subscribe(
      (res: any[]) => {
        this.chevalList = res;
        if (this.chevalList.length == 0) {
          this.loading = false;
          this.messageService.add({ severity: 'warn', summary:'Attention', detail: 'Aucun cheval trouvé.' });
        } else {
          for (let element of this.chevalList) {
            let params = {
              idCheval: element["id"]
            }
            // if (element["etat"] == "AENTR") {
            //   element["etat"] = "A l'Entrainement";
            // }
            // if (element["etat"] == "HENTR") {
            //   element["etat"] = "Hors Entrainement";
            // }
            // if (element["etat"] == "SORTI") {
            //   element["etat"] = "Sortie d'entrainement";
            // }
            this.http.search(environment.apiPersonneM + "search/relation", params).subscribe(
              (res: any[]) => {
                if (res.length == 1) {
                  for (let item of res) {
                    element["nomProprietaire"] = item["nom"] + " " + item["prenom"];
                    element["nomEntraineur"] = item["nom"] + " " + item["prenom"];
                  }
                } else if (res.length == 2) {
                  for (let item of res) {
                    if (item["codeNatureRelation"] == "PROPR") {
                      switch (item["codeNaturePersonne"]) {
                        case "P": {
                          element["nomProprietaire"] = item["nom"] + " " + item["prenom"];
                          break;
                        }
                        case "M": {
                          element["nomProprietaire"] = item["raisonSociale"];
                          break;
                        }
                        case "A": {
                          element["nomProprietaire"] = item["designation"];
                          break;
                        }
                        default: {
                          break;
                        }
                      }
                    }
                    if (item["codeNatureRelation"] == "ENTRA") {
                      element["nomEntraineur"] = item["nom"] + " " + item["prenom"];
                    }
                  }
                }
              }
            );
          }
        }
      }
    );
    setTimeout(() => {
      this.tableDisplay = true;
      this.loading = false;
    }, 1600);
  }

  addItem() {
    this.addLoading = false;
    this.display = true;
    this.action = "Ajouter";
    this.reset();
    this.numeroEsrima = '';
  }

  detailsItem(cheval: Cheval) {
    this.addLoading = false;
    this.action = "Details";
    this.numeroEsrima = cheval.numeroEsrima;
    this.numeroTranspondeur = cheval?.transpondeur;
    this.nom = cheval?.nom;
    this.code = cheval?.code;
    this.etat = cheval?.libelleEtat;
    this.dateEtat = cheval?.dateEtat;
    this.nomEntraineur = cheval?.nomEntraineur;
    this.nomProprietaire = cheval?.nomProprietaire;
    this.display = true;

  }

  historiqueReservationItem(cheval: Cheval) {
    this.http.get(apiReservation + "cheval/" + cheval["id"]).subscribe(
      (res: any[]) => {
        this.reservations = res;
        for (let element of this.reservations) {
          if (element["chevalBoxs"].length >= 1) {
            element["type"] = "Hebergement Cheval";
          } else {
            element["type"] = "Entrainement Cheval";
          }
        }
      }
    );
    this.historiqueReservationsDisplay = true;
  }

  deleteItem(cheval: Cheval) {
    this.confirmationService.confirm({
      message: 'Etes-vous sûr de vouloir supprimer ce cheval ?',
      accept: () => {
        this.http.delete(apiCheval, cheval["id"]).subscribe(res => {
          this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: "Cheval supprimé" });
          setTimeout(() => {
          }, 1500);
        }, error => {
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: error.error.message });
          setTimeout(() => {
          }, 1500);
        });
      }
    });
  }

  reset() {
    this.numeroTranspondeur = '';
    this.code = '';
    this.etat = '';
    this.dateEtat = '';
    this.nom = '';
    this.nomEntraineur = '';
    this.prenomEntraineur = '';
    this.nomProprietaire = '';
    this.prenomProprietaire = '';
    // this.addLoading = false;
  }

  onNumeroEsrimaEnter() {
    this.reset();
    this.addLoading = true;
    if (this.numeroEsrima.length > 3) {
      this.reset();
      let params = {
        numeroEsrima: this.numeroEsrima != '' ? this.numeroEsrima : '',
        nomCheval: this.nom != '' ? this.nom : ''
      }

      let pipe = new DatePipe('en-US');

      this.http.search(environment.apiCheval + "search/esrima", params).subscribe(
        (res: any[]) => {
          this.chevals = res;
          if (this.chevals.length == 1) {
            this.id = this.chevals[0]["id"];
            this.nom = this.chevals[0]["nom"];
            this.numeroEsrima = this.chevals[0]["numeroEsrima"] != '' ? this.chevals[0]["numeroEsrima"] : '';
            this.numeroTranspondeur = this.chevals[0]["transpondeur"] = '' ? this.chevals[0]["transpondeur"] : '';
            this.code = this.chevals[0]["codeCheval"] != '' ? this.chevals[0]["codeCheval"] : '';
            this.etat = this.chevals[0]["libelleEtat"];
            // if (this.chevals[0]["etat"] == "AENTR") {
            //   this.etat = "A l'Entrainement";
            // } else if (this.chevals[0]["etat"] == "HENTR") {
            //   this.etat = "Hors Entrainement";
            // } else {
            //   this.etat = "Sortie d'Entrainement";
            // }
            this.dateEtat = this.chevals[0]["dateEtat"] != '' ? pipe.transform(this.chevals[0]["dateEtat"], 'short') : '';
            this.dateEtatDate = this.chevals[0]["dateEtat"];
            this.loadChevalInfos(this.id);
            setTimeout(() => {
              this.addLoading = false;
            }, 500);

          } else if (this.chevals.length > 1) {
            this.listeSearchDisplay = true;
          } else {
            this.addLoading = false;
            this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Aucun cheval trouvé ! ' });
          }
        }
      );
    } else {
      this.addLoading = false;
      this.messageService.add({ severity: 'warn', summary: 'Attention', detail: 'Merci de fournir au moins 4 chiffres.' });
    }
  }

  loadChevalInfos(id: any) {
    let params = {
      idCheval: id
    }
    this.http.search(environment.apiPersonneM + "search/relation", params).subscribe(
      (res: any[]) => {
        if (res.length == 1) {
          for (let element of res) {
            this.nomEntraineur = element["nom"] + " " + element["prenom"];
            this.nomProprietaire = element["nom"] + " " + element["prenom"];
          }
        } else if (res.length == 2) {
          for (let element of res) {
            if (element["codeNatureRelation"] == "PROPR") {
              switch (element["codeNaturePersonne"]) {
                case "P": {
                  this.nomProprietaire = element["nom"] + " " + element["prenom"];
                  break;
                }
                case "M": {
                  this.nomProprietaire = element["raisonSociale"];
                  break;
                }
                case "A": {
                  this.nomProprietaire = element["designation"];
                  break;
                }
                default: {
                  break;
                }
              }
            }
            if (element["codeNatureRelation"] == "ENTRA") {
              this.nomEntraineur = element["nom"] + " " + element["prenom"];
            }
          }
        }
      }
    );
  }

  closeSelectionDialog() {
    this.listeSearchDisplay = false;
    this.addLoading = false;
  }

  closeDialog() {
    this.reset();
    this.numeroEsrima = '';
    this.display = false;
  }

  selectionner(cheval: any) {
    let pipe = new DatePipe('en-US');
    this.nom = cheval.nom;
    this.numeroEsrima = cheval?.numeroEsrima;
    this.numeroTranspondeur = cheval?.transpondeur;
    this.code = cheval?.code;
    this.etat = this.chevals[0]["libelleEtat"];
    // if (this.chevals[0]["etat"] == "AENTR") {
    //   this.etat = "A l'Entrainement";
    // } else if (this.chevals[0]["etat"] == "HENTR") {
    //   this.etat = "Hors Entrainement";
    // } else {
    //   this.etat = "Sortie d'Entrainement";
    // }
    this.dateEtat = pipe.transform(cheval?.dateEtat, 'short');
    this.dateEtatDate = this.chevals[0]["dateEtat"];
    this.id = cheval?.id;
    this.loadChevalInfos(this.id);
    this.addLoading = false;
    this.listeSearchDisplay = false;


  }

  saveItem() {
    let cheval = {
      id: this.id,
      nom: this.nom,
      codeCheval: this.code,
      numeroEsrima: this.numeroEsrima,
      numeroTranspondeur: this.numeroTranspondeur,
      etat: this.etat,
      dateEtat: this.dateEtatDate
    }

    this.http.post(environment.apiCheval, cheval).subscribe(
      res => {
        this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Nouveau cheval ajouté.' });
        this.numeroEsrima = '';
        setTimeout(() => {
          this.display = false;
        }, 2000);
      }, error => {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: error.error.message });
      }
    );
  }

  closeHistoriqueReservationsDialog() {
    this.reservations = [];
    this.historiqueReservationsDisplay = false;
  }

}
