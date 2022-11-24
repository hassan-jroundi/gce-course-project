import { HttpClient } from '@angular/common/http';
import { element } from 'protractor';
import { environment } from 'src/environments/environment';
import { SessionService } from 'src/app/core/services/session.service';
import { Component, OnInit } from '@angular/core';
import {MenuItem, MessageService, TreeNode} from 'primeng/api';
import { RaisonSociale } from 'src/app/core/models/raison-sociale.model';
import { RouteStateService } from 'src/app/core/services/route-state.service';
import { DatePipe } from '@angular/common';
import { DataJson } from 'src/app/core/models/data-json.model';
import { ReservationOutput } from 'src/app/core/models/reservation-output';
import { TypeReservation } from 'src/app/core/models/type-reservation.model';
import { ModePaiement } from 'src/app/core/models/mode-paiement';
import { HttpService } from 'src/app/core/services/http.service';

let apiReservation = environment.apiReservation;
let apiPersonneLit = environment.apiPersonneLit;
let apiChevalBox = environment.apiChevalBox;
let apiChevalPiste = environment.apiChevalPiste;
let apiStatutReservation = environment.apiStatutReservation;
let apiGenerateReport = environment.apiGenerateReport;
let apiPrixLit = environment.apiPrixLit;
let apiPrixBox = environment.apiPrixBox;
let apiPrixPiste = environment.apiPrixPiste;
let apiFacture = environment.apiFacture;
let apiPersonneM = environment.apiPersonneM;
let apiDetailReservation = environment.apiDetailReservation;

@Component({
  selector: 'app-facturation-personne',
  templateUrl: './facturation-personne.component.html'
})
export class FacturationPersonneComponent implements OnInit {

  selectedReservations: any[] = [];
  selectedDetailReservations: any[] = [];
  selectedReservationsFacture: any[] = [];
  selectedFactures: any[] = [];

  items: MenuItem[];

  home: MenuItem;

  testos: TreeNode[];

  reservations: ReservationOutput[] = [];
  reservationFactures: ReservationOutput[] = [];
  factures: any[] = [];

  choixRaisonSociale: any;
  typeReservation: TypeReservation[];
  typeReservationCatched: any;
  typeReservationCatchedDetail: any = '';
  type: any;
  statutReservationSelected: any = '';

  loading: boolean = false;
  display: boolean = false;
  tableDisplay: boolean = false;
  listeSearchDisplay: boolean = false;
  selectionDisplay: boolean = false;
  panelInfosDisplay: boolean = false;
  detailsDisplay: boolean = false;
  modePaiementDisplay: boolean = false;
  factureDetailsDisplay: boolean = false;

  personnes: any[];
  personneAFacturers: any[];
  raisonSocialeList: RaisonSociale[] = [
    { name: 'Personne Physique', code: 'P' },
    { name: 'Personne Morale', code: 'M' },
    { name: 'Association', code: 'A' }
  ];
  modePaiementList: ModePaiement[] = [
    { name: 'Espèce', code: 'E' },
    { name: 'Chèque', code: 'C' },
    { name: 'Versement', code: 'V' }
  ];
  modePaiementCatched: ModePaiement;
  raisonSocialeCatched: RaisonSociale;

  //Search filter
  nomSearch: any = '';
  prenomSearch: any = '';
  cinSearch: any = '';
  raisonSocialeSearch: any = '';
  designationSearch: any = '';
  typeRaisonSocialeSearch: any;

  id: any;
  nom: any;
  prenom: any;
  cin: any;
  designation: any;
  raisonSociale: any;
  email: any;
  telephone: any;
  adresse: any;
  nomGerant: any;

  //Détails facture
  numeroFactureDetails: any = '';
  modePaiementFactureDetails: any = '';
  operateurFactureDetails: any = '';
  dateMiseEnFactureDetailsDate: Date = new Date();
  dateMiseEnFactureDetails: any = '';
  datePaiementFactureDetails: any = '';
  statutFactureDetails: any = '';
  montantHTFactureDetails: any = '';

  //Détails réservation
  numeroReservationDetail: any = '';
  typeReservationDetail: any = '';
  dateDebutReservationDetail: any = '';
  dateFinReservationDetail: any = '';
  nbrJoursReservationDetail: any = '';
  nbrHeuresReservationDetail: any = '';
  statutReservationDetail: any = '';
  nomChevalReservationDetail: any = '';
  numeroEsrimaChevalReservationDetail: any = '';
  nomPersonneReservationDetail: any = '';
  prenomPersonneReservationDetail: any = '';
  cinPersonneReservationDetail: any = '';
  prixHeureReservationDetail: any = '';
  prixJourReservationDetail: any = '';
  montantReservationDetail: any = '';

  nombre: any = '';
  resScrollable: boolean = false;
  facScrollable: boolean = false;


  constructor(private routeStateService: RouteStateService,
    private messageService: MessageService,
    private sessionService: SessionService,
    private http: HttpService,
    private httpClient: HttpClient,
    private datePipe: DatePipe) {
    this.items = [];
    // this.raisonSocialeList = [
    //   { name: 'Personne Physique', code: 'P' },
    //   { name: 'Personne Morale', code: 'M' },
    //   { name: 'Association', code: 'A' }
    // ];
    this.typeReservation = [
      { name: 'Hébergement Personne', code: 'P' },
      { name: 'Hébergement Cheval', code: 'C' },
      { name: 'Entrainement Cheval', code: 'E' }
    ]
  }

  ngOnInit() {
    var routes = this.routeStateService.getAll();
    routes.forEach(route => {
      this.items.push({ label: route.title, command: () => { this.onClickBreadcrumb(route.id); } });
    });

    this.home = { icon: 'pi pi-home' };
  }

  onClickBreadcrumb(id: number) {
    this.routeStateService.loadById(id);
  }

  onRaisonSocialeChange(event) {
    this.tableDisplay = false;
    this.raisonSocialeCatched = event.value;
    this.choixRaisonSociale = this.raisonSocialeCatched?.code;
    this.resetSearchInpus();
    this.resetNonSearchInputs();
    this.panelInfosDisplay = false;
  }

  reset() {
    this.type = null;
    this.reservations = [];
    this.panelInfosDisplay = false;
    this.resetSearchInpus();
    this.resetNonSearchInputs();
  }

  resetSearchInpus() {
    this.nomSearch = '';
    this.prenomSearch = '';
    this.raisonSocialeSearch = '';
    this.designationSearch = '';
    this.cinSearch = '';
  }

  doSearch() {
    this.tableDisplay = false;
    this.reservations = [];
    // this.loading = true;
    if (this.nomSearch.length == 0 && this.prenomSearch.length == 0 && this.cinSearch.length == 0 && this.designationSearch.length == 0 && this.raisonSocialeSearch.length == 0) {
      this.messageService.add({severity: 'warn', summary: 'Attention', detail: 'Merci de saisir une valeur pour la recherche.'});
      setTimeout(() => {
        this.loading = false;
      }, 800);
    } else {
      this.loading = true;
      let params = {
        nom: this.nomSearch != '' ? this.nomSearch : '',
        prenom: this.prenomSearch != '' ? this.prenomSearch : '',
        numeroPieceIdentite: this.cinSearch != '' ? this.cinSearch : '',
        designation: this.designationSearch != '' ? this.designationSearch : '',
        raisonSociale: this.raisonSocialeSearch != '' ? this.raisonSocialeSearch : ''
      }

      this.http.search(apiPersonneM + 'search2', params).subscribe(
        (res: any[]) => {
          this.personneAFacturers = res;
          if (this.personneAFacturers.length == 1) {
            this.id = this.personneAFacturers[0]["id"];
            this.nom = this.personneAFacturers[0]["nom"];
            this.prenom = this.personneAFacturers[0]["prenom"];
            this.designation = this.personneAFacturers[0]["designation"];
            this.raisonSociale = this.personneAFacturers[0]["raisonSociale"];
            this.cin = this.personneAFacturers[0]["numeroPieceIdentite"];
            this.nomGerant = this.personneAFacturers[0]["nomGerant"];
            this.email = this.personneAFacturers[0]["email"];
            this.adresse = this.personneAFacturers[0]["adresse1"];
            this.telephone = this.personneAFacturers[0]["numeroTelephone1"];
            this.reservations = this.personneAFacturers[0]["reservations"];
            for (let element of this.reservations) {

              if (element["chevalBoxs"].length > 0) {
                element["type"] = "Hebergement Cheval";
                element["nomConcerne"] = (element["chevalBoxs"][0]["cheval"] != null ? element["chevalBoxs"][0]["cheval"]["nom"] : "Aucun cheval");
              }
              if (element["personneLits"].length > 0) {
                element["type"] = "Hébergement Personne";
                element["nomConcerne"] = (element["personneLits"][0]["personne"] != null ? element["personneLits"][0]["personne"]["nom"] + " " + element["personneLits"][0]["personne"]["prenom"] : "Aucune personne");
              }
            }
            this.http.get(apiFacture + "personneAFacturer/" + this.id).subscribe((resu: any[]) => {
              this.factures = resu;
            });
            setTimeout(() => {
              this.loading = false;
              this.display = true;
              this.panelInfosDisplay = true;
            }, 800);

          } else if (this.personneAFacturers.length > 1) {
            this.loading = false;
            this.selectionDisplay = true;
          } else {
            this.loading = false;
            this.messageService.add({severity: 'error', summary: 'Erreur', detail: 'Aucune personne trouvée !'});
            this.resetSearchInpus();
            this.resetNonSearchInputs();
            setTimeout(() => {
              this.loading = false;
            }, 800);
          }
        }
      );
    }
  }

  resetNonSearchInputs() {
    this.nom = '';
    this.prenom = '';
    this.cin = '';
    this.designation = '';
    this.raisonSociale = '';
    this.adresse = '';
    this.email = '';
    this.telephone = '';
    this.nomGerant = '';
  }

  closeSelectionDialog() {
    this.selectionDisplay = false;
  }

  selectionner(personneAFacturer: any) {
    this.http.get(apiPersonneM + "personne/" + personneAFacturer.id).subscribe(
      res => {
        this.loading = false;
        this.id = res["id"];
        this.nom = res["nom"];
        this.prenom = res["prenom"];
        this.designation = res["designation"];
        this.raisonSociale = res["raisonSociale"];
        this.cin = res["numeroPieceIdentite"];
        this.nomGerant = res["nomGerant"];
        this.email = res["email"];
        this.adresse = res["adresse1"];
        this.telephone = res["numeroTelephone1"];
        this.reservations = res["reservations"];
        for (let element of this.reservations) {

          if (element["chevalBoxs"].length > 0) {
            element["type"] = "Hebergement Cheval";
            element["nomConcerne"] = (element["chevalBoxs"][0]["cheval"] != null ? element["chevalBoxs"][0]["cheval"]["nom"] : "Aucun cheval");
          }
          if (element["personneLits"].length > 0) {
            element["type"] = "Hébergement Personne";
            element["nomConcerne"] = (element["personneLits"][0]["personne"] != null ? element["personneLits"][0]["personne"]["nom"] + " " + element["personneLits"][0]["personne"]["prenom"] : "Aucune personne");
          }
        }
        this.http.get(apiFacture + "personneAFacturer/" + this.id).subscribe((resu: any[]) => {
          this.factures = resu;
        });
      }
    );
    setTimeout(() => {
    this.panelInfosDisplay = true;
    this.selectionDisplay = false;
    this.display = true;
    }, 500);
  }

  detailsItem(reservation: any) {
    this.typeReservationDetail = reservation["type"];
    this.dateDebutReservationDetail = reservation["dateDebut"];
    this.dateFinReservationDetail = reservation["dateFin"];
    this.numeroReservationDetail = reservation["numero"];
    this.statutReservationDetail = reservation["statut"];
    if (reservation["type"].includes("Personne")) {
      this.nombre = "J";
      this.typeReservationCatchedDetail = "P";
      this.http.get(apiReservation + reservation["numero"]).subscribe(res => {
        this.nomPersonneReservationDetail = res["personneLits"][0]["personne"]["nom"];
        this.prenomPersonneReservationDetail = res["personneLits"][0]["personne"]["prenom"];
        this.cinPersonneReservationDetail = res["personneLits"][0]["personne"]["numeroPieceIdentite"];
        this.http.get(apiPersonneLit + "nombreJours/" + res["personneLits"][0]["id"]).subscribe(resu => {
          this.nbrJoursReservationDetail = resu;
        });

      });
    }
    if (reservation["type"].includes("Cheval")) {
      this.typeReservationCatchedDetail = "C";
      this.http.get(apiReservation + reservation["numero"]).subscribe(res => {
        if (res["chevalBoxs"].length > 0) {
          this.nombre = "J";
          this.nomChevalReservationDetail = res["chevalBoxs"][0]["cheval"]["nom"];
          this.numeroEsrimaChevalReservationDetail = res["chevalBoxs"][0]["cheval"]["numeroEsrima"];
          this.http.get(apiChevalBox + "nombreJours/" + res["chevalBoxs"][0]["id"]).subscribe(resu => {
            this.nbrJoursReservationDetail = resu;
          });
        }
        if (res["chevalPistes"].length > 0) {
          this.nombre = "H";
          this.nomChevalReservationDetail = res["chevalPistes"][0]["cheval"]["nom"];
          this.numeroEsrimaChevalReservationDetail = res["chevalPistes"][0]["cheval"]["numeroEsrima"];
          this.http.get(apiChevalPiste + "nombreHeures/" + res["chevalPistes"][0]["id"]).subscribe(resu => {
            this.nbrHeuresReservationDetail = resu;
          });
        }
      });
    }
    this.detailsDisplay = true;

  }

  closeDetailsDialog() {
    this.detailsDisplay = false;
    this.resetDetailsDialog();
  }

  closeFactureDetailsDialog() {
    this.reservationFactures = [];
    this.factureDetailsDisplay = false;
    this.resetFactureDetailsDialog();
  }

  detailsFacture(facture: any) {
    this.reservationFactures = [];
    this.numeroFactureDetails = facture.id;
    this.datePaiementFactureDetails = facture.datePaiement;
    this.dateMiseEnFactureDetailsDate = new Date(facture.dateFacture);
    this.dateMiseEnFactureDetails = this.datePipe.transform(this.dateMiseEnFactureDetailsDate, 'dd/MM/yyyy');
    this.operateurFactureDetails = facture.nomOperateur;
    this.statutFactureDetails = facture.libelleStatut;
    this.modePaiementFactureDetails = facture.libelleModePaiement;
    this.http.get(apiDetailReservation + "facture/" + facture.id).subscribe((res: any[]) => {
      this.reservationFactures = res;
    });
    setTimeout(() => {
      this.factureDetailsDisplay = true;
    }, 300);
  }

  resetFactureDetailsDialog() {
    this.numeroFactureDetails = '';
    this.modePaiementFactureDetails = '';
    this.operateurFactureDetails = '';
    this.dateMiseEnFactureDetails = '';
    this.datePaiementFactureDetails = '';
    this.statutFactureDetails = '';
  }

  resetDetailsDialog() {
    this.nomChevalReservationDetail = "";
    this.numeroReservationDetail = "";
    this.typeReservationDetail = "";
    this.dateDebutReservationDetail = "";
    this.dateFinReservationDetail = "";
    this.nbrHeuresReservationDetail = "";
    this.nbrJoursReservationDetail = "";
    this.statutReservationDetail = "";
    this.nomPersonneReservationDetail = "";
    this.prenomPersonneReservationDetail = "";
    this.cinPersonneReservationDetail = "";
    this.numeroEsrimaChevalReservationDetail = "";
  }

  genererFacture() {
    let dataJson = new DataJson();
    let data: string = "[";
    dataJson["reportName"] = "test";
    dataJson["format"] = "pdf";
    let jasperDocument = {
      "idCase": "Non renseigné",
      "numClient": "Non renseigné",
      "produit": "Non renseigné",
      "caseTitle": "Non renseigné",
      "dateNumerisation": new Date()
    };
    data = data + JSON.stringify(jasperDocument) + "]";
    dataJson["data"] = data;
    this.http.printReport(apiGenerateReport, dataJson).subscribe(res => {
      const blob = new Blob([res], { type: 'application/x-download' });
      if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(blob);
        return;
      }
      const data = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = data;
      link.download = 'test.pdf';
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));
      setTimeout(() => {
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    })
  }

  onRowSelect(event) {
  }

  telechargerFacture(facture: any) {
    this.http.printReportBis(apiGenerateReport + "facture/" + facture["id"]).subscribe(result => {
      const blob = new Blob([result as BlobPart], { type: 'application/pdf' });
      if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(blob);
        return;
      }
      const data = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = data;
      link.download = 'facture-' + facture["id"] + '.pdf';
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));
      setTimeout(() => {
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    }, error => {
    });
  }

  openModePaiementDisplay() {
    this.modePaiementDisplay = true;
  }

  facturerUneReservation(reservationId: any) {
    let reservationIds = [];
    reservationIds.push(this.modePaiementCatched.code);
    reservationIds.push(this.sessionService.getItem("currentUser")["login"]);
    reservationIds.push(reservationId);
    this.http.post(apiReservation + "facturer", reservationIds).subscribe(res => {
      this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Facturation effectuée avec succès.'});
      setTimeout(() => {
        this.reservations = [];
        this.modePaiementDisplay = false;
        this.detailsDisplay = false;
        this.http.get(apiReservation + "personneAFacturer/" + this.personneAFacturers[0]["personne"]["id"]).subscribe((res: any[]) => {
          let reservations = res;
          for (let element of reservations) {
            let reservation = new ReservationOutput();
            reservation.numero = element["id"];
            this.httpClient.get<string>(apiStatutReservation + element["statutReservation"]).subscribe((res) => {
              reservation.statut = res["designation"];
              if (element["chevalBoxs"].length > 0) {
                reservation.type = "Hébergement Cheval";
                reservation.dateDebut = element["chevalBoxs"][0]["dateDebut"];
                reservation.dateFin = element["chevalBoxs"][0]["dateFin"];
                reservation.nomConcerne = element["chevalBoxs"][0]["cheval"]["nom"] + " " + "(C)";
                this.typeReservationCatched = "C";
              }
              if (element["chevalPistes"].length > 0) {
                let pipe = new DatePipe('en-US');
                reservation.type = "Entrainement Cheval";
                reservation.dateDebut = element["chevalPistes"][0]["dateDebut"];
                reservation.dateFin = element["chevalPistes"][0]["dateFin"];
                reservation.nomConcerne = element["chevalPistes"][0]["cheval"]["nom"] + " " + "(C)";
                this.typeReservationCatched = "E";
              }
              if (element["personneLits"].length > 0) {
                reservation.type = "Hébergement Personne";
                reservation.dateDebut = element["personneLits"][0]["dateDebut"];
                reservation.dateFin = element["personneLits"][0]["dateFin"];
                reservation.nomConcerne = element["personneLits"][0]["personne"]["nom"] + " " + element["personneLits"][0]["personne"]["prenom"] + " " + "(P)";
                this.typeReservationCatched = "P";
              }
              this.reservations.push(reservation);
            });
            this.http.get(apiFacture + "personneAFacturer/" + this.personneAFacturers[0]["personne"]["id"]).subscribe((res: any[]) => {
              this.factures = res;
            });
          }
        });
      }, 1600);
    });
  }

  facturer(selectedDetailReservations: any) {
    if (this.numeroReservationDetail == '') {
      let reservationIds = [];
      // reservationIds.push(this.modePaiementCatched.code);
      reservationIds.push(this.sessionService.getItem("currentUser")["id"]);
      reservationIds.push(this.sessionService.getItem("currentUser")["idSession"]);
      reservationIds.push(this.id);
      for (let element of this.selectedDetailReservations) {
        reservationIds.push(element["id"]);
      }
      this.http.post(apiReservation + "facturer", reservationIds).subscribe(res => {
        this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Facturation effectuée avec succès.'});
        setTimeout(() => {
          // this.modePaiementDisplay = false;
          this.reset();
        }, 1600);
      }, error => {
        this.messageService.add({severity: 'warn', summary: 'Attention', detail: 'Il n\'est pas possible de facturer des réservations avec des types de prix différents.'});
      });
    } else {
      this.facturerUneReservation(this.numeroReservationDetail);
    }
  }

  mettreEnPayeFacture(selectedFactures: any) {
    for (let element of selectedFactures) {
      this.http.get(apiFacture + "mettreEnPaye/" + element["id"] + "/session/" + this.sessionService.getItem("currentUser")["idSession"] + "/modePaiement/" + this.modePaiementCatched.code).subscribe(res => {
        this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Mise en payé effectuée avec succès.'});
      });
      setTimeout(() => {
        this.modePaiementDisplay = false;
        this.reset();
      }, 1600);
    }
  }
}
