import {SessionService} from './../../../core/services/session.service';
import {HttpService} from 'src/app/core/services/http.service';
import {Component, OnInit} from '@angular/core';
import {environment} from 'src/environments/environment';
import {ModePaiement} from 'src/app/core/models/mode-paiement';
import {MessageService} from 'primeng/api';

let apiChevalBox = environment.apiChevalBox;
let apiChevalPiste = environment.apiChevalPiste;
let apiPersonneLit = environment.apiPersonneLit;
let apiDetailReservation = environment.apiDetailReservation;
let apiReservation = environment.apiReservation;
let apiGenerateReport = environment.apiGenerateReport;
let apiFacture = environment.apiFacture;

@Component({
  selector: 'app-facturation-mois',
  templateUrl: './facturation-mois.component.html',
  styleUrls: ['./facturation-mois.component.scss']
})
export class FacturationMoisComponent implements OnInit {

  modePaiementList: ModePaiement[] = [
    {name: 'Espèce', code: 'E'},
    {name: 'Chèque', code: 'C'},
    {name: 'Versement', code: 'V'}
  ];
  modePaiementCatched: ModePaiement;

  dateValue: Date = new Date();

  personneLits: any[] = [];
  chevalBoxs: any[] = [];
  detailReservations: any[] = [];
  detailReservationSelected: any = '';

  hebergementPersonneDisplay: boolean = false;
  hebergementChevalDisplay: boolean = false;
  searchDisplay: boolean = false;
  loading: boolean = false;
  modePaiementDisplay: boolean = false;

  selectedPersonneLits: any[] = [];
  selectedChevalBoxs: any[] = [];

  constructor(private http: HttpService, private sessionService: SessionService, private messageService: MessageService) {
  }

  ngOnInit() {
  }

  doSearch() {
    this.chevalBoxs = [];
    this.personneLits = [];
    this.hebergementChevalDisplay = false;
    this.hebergementPersonneDisplay = false;
    if (this.dateValue == null) {
      this.messageService.add({severity: 'warn', summary: 'Attention', detail: 'Merci de choisir un mois pour effectuer la recherche.'});
    } else {
      if (this.searchDisplay) {
        this.searchDisplay = false;
      } else {
        this.loading = true;
        this.http.get(apiDetailReservation + 'date/' + this.dateValue).subscribe(
          (res: any[]) => {
            this.detailReservations = res;
            for (let element of this.detailReservations) {
              element['dateDebut'] = new Date(element['dateDebut']);
              element['dateFin'] = new Date(element['dateFin']);
              if (element['libelleTypeReservation'] == 'Hébergement Cheval') {
                this.chevalBoxs.push(element);
              }
              if (element['libelleTypeReservation'] == 'Hébergement Personne') {
                this.personneLits.push(element);
              }
            }
            this.personneLits.sort((a, b) => (a.nomPersonneAFacturer < b.nomPersonneAFacturer ? -1 : 1));
            this.chevalBoxs.sort((a, b) => (a.nomPersonneAFacturer < b.nomPersonneAFacturer ? -1 : 1));
          }
        );
        setTimeout(() => {
          // this.searchDisplay = true;
          this.loading = false;
          this.hebergementChevalDisplay = true;
          this.hebergementPersonneDisplay = true;
        }, 500);
      }
    }
  }

  reset() {
    this.searchDisplay = false;
    this.hebergementChevalDisplay = false;
    this.hebergementPersonneDisplay = false;
    this.dateValue = new Date();
    this.detailReservations = [];
    this.chevalBoxs = [];
  }

  detailsReservation(detailReservation: any) {
  }

  telechargerFacture(detailReservation: any) {
    this.http.printReportBis(apiGenerateReport + "facture/" + detailReservation.facture.id).subscribe(result => {
      const blob = new Blob([result as BlobPart], { type: 'application/pdf' });
      if (window.navigator && window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(blob);
        return;
      }
      const data = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = data;
      link.download = 'facture-' + detailReservation.facture.id + '.pdf';
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));
      setTimeout(() => {
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100);
    }, error => {
    });
  }

  openModePaiementDisplay(detailReservation: any) {
    this.modePaiementDisplay = true;
    this.detailReservationSelected = detailReservation;
  }

  mettreEnPaye() {
    this.http.get(apiFacture + "mettreEnPaye/" + this.detailReservationSelected["facture"]["id"] + "/session/" + this.sessionService.getItem("currentUser")["idSession"] + "/modePaiement/" + this.modePaiementCatched.code).subscribe(res => {
      this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Mise en payé effectuée avec succès.'});
    });
    setTimeout(() => {
      this.modePaiementDisplay = false;
      this.reset();
    }, 1600);
  }

  closeModePaiementDisplay() {
    this.modePaiementDisplay = false;

  }

  facturer() {

    let personneAFacturerIds = [];
    let reservationIds = [];
    // reservationIds.push(this.modePaiementCatched.code);
    reservationIds.push(this.sessionService.getItem('currentUser')['id']);
    reservationIds.push(this.sessionService.getItem('currentUser')['idSession']);
    if (this.selectedChevalBoxs.length > 0) {
      reservationIds.push(this.selectedChevalBoxs[0]["idPersonneAFacturer"]);
    } else {
      reservationIds.push(this.selectedPersonneLits[0]["idPersonneAFacturer"]);
    }
    for (let element of this.selectedChevalBoxs) {
      personneAFacturerIds.push(element["idPersonneAFacturer"]);
      reservationIds.push(element["id"]);
    }
    for (let element of this.selectedPersonneLits) {
      personneAFacturerIds.push(element["idPersonneAFacturer"]);
      reservationIds.push(element["id"]);
    }

    const allEqual = arr => arr.every( v => v === arr[0] )

    if (allEqual(personneAFacturerIds)) {
        this.http.post(apiReservation + 'facturer', reservationIds).subscribe(res => {
          this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Facturation effectuée.'});
          setTimeout(() => {
            this.reset();
          }, 1600);
        });
    } else {
      this.messageService.add({severity: 'warn', summary: 'Attention', detail: "Il n'est pas possible de facturer des réservations avec des personnes à facturer différentes."});
    }
  }

}
