import {SessionService} from 'src/app/core/services/session.service';
import {SiteService} from './../../../core/services/site.service';
import {MessageService} from 'primeng/api';
import {PrixPiste} from './../../../core/models/prix-piste.model';
import {Piste} from 'src/app/core/models/piste.model';
import {environment} from 'src/environments/environment';

import {HttpService} from './../../../core/services/http.service';
import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ConfirmationService} from 'primeng';

let apiPiste = environment.apiPiste;
let apiPrixPiste = environment.apiPrixPiste;

@Component({
  selector: 'app-ressources-piste',
  templateUrl: './ressources-piste.component.html',
  styleUrls: ['ressources-piste.component.css']
})
export class RessourcesPisteComponent implements OnInit {

  pistes: Piste[];
  prixPistes: PrixPiste[];

  display: boolean = false;
  historiqueDisplay: boolean = false;
  action: any;

  idPiste: any;
  codePiste: any;
  prixUnitairePiste: any;
  prixForfaitairePiste: any;
  sitePiste: any;
  prixPisteObject: any;
  prixPisteTem: any;

  prixActuel: any;
  dateDebutPrixActuel: any;

  profilUtilisateurConnecte: any;

  constructor(private httpService: HttpService,
              private http: HttpService,
              private confirmationService: ConfirmationService,
              private messageService: MessageService,
              private siteService: SiteService,
              private sessionService: SessionService) {
    this.profilUtilisateurConnecte = this.sessionService.getItem('codeProfil');
  }

  ngOnInit() {

    this.loadPisteList();

  }

  loadPisteList() {
    this.httpService.get(environment.apiPiste + '/site/' + this.sessionService.getItem('site')['nom']).subscribe(
      (res: Piste[]) => {
        this.pistes = res;
        for (let element of this.pistes) {
          let params = {
            id: element['id']
          };
          this.httpService.get(environment.apiPrixPiste + 'actuel/' + element['id']).subscribe(
            resultat => {
              element['prix'] = resultat['montant'] ? resultat['montant'] : '';
            }
          );
        }

      }
    );
  }

  editItem(piste: Piste) {
    this.action = 'Modifier';
    this.display = true;
    this.codePiste = piste.nom;
    this.prixUnitairePiste = piste.prix;
    this.idPiste = piste.id;
    this.sitePiste = piste.site;
    this.prixPisteTem = this.prixUnitairePiste;

    let params = {
      nom: this.codePiste
    };
    this.httpService.get(environment.apiPrixPiste + 'actuel/' + this.idPiste).subscribe(
      res => {
        this.prixPisteObject = res;
      }
    );
  }

  deleteItem(piste: Piste) {
    this.confirmationService.confirm({
      message: 'Etes-vous sûr de vouloir supprimer cette piste ?',
      accept: () => {
        this.http.delete(apiPiste, piste['id']).subscribe(res => {
          this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Piste supprimée'});
          setTimeout(() => {
            this.loadPisteList();
          }, 500);
        }, error => {
          this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Il n'est pas possible de supprimer une piste liée à une réservation."});
          setTimeout(() => {
          }, 1500);
        });
      }
    });

  }

  addItem() {
    this.action = 'Ajouter';
    this.prixUnitairePiste = '';
    this.codePiste = '';
    this.display = true;

  }

  saveItem() {
    if (this.action == 'Ajouter') {
      let duplicatedName = false;
      for (let element of this.pistes) {
        if (this.codePiste == element['nom']) {
          duplicatedName = true;
          break;
        }
      }
      if (duplicatedName == true) {
        this.messageService.add({severity: 'error', summary: 'Erreur', detail: 'Le nom de la piste existe déjà.'});
      } else {
        let piste = {
          nom: this.codePiste,
          site: this.sessionService.getItem('site'),
          isActif: true,
          idSession: this.sessionService.getItem('currentUser')['idSession']
        };
        this.http.post(apiPiste, piste).subscribe(
          res => {
            let prixUnitairePiste = {
              idPiste: res['id'],
              montant: this.prixUnitairePiste,
              dateDebut: new Date(),
              dateFin: null,
              typePrix: 'U',
              idSession: this.sessionService.getItem('currentUser')['idSession']
            };
            this.http.post(apiPrixPiste, prixUnitairePiste).subscribe(
              resu => {
              }
            );
            let prixForfaitairePiste = {
              idPiste: res['id'],
              montant: this.prixForfaitairePiste,
              dateDebut: new Date(),
              dateFin: null,
              typePrix: 'F',
              idSession: this.sessionService.getItem('currentUser')['idSession']
            };
            this.http.post(apiPrixPiste, prixForfaitairePiste).subscribe(
              resu => {
              }
            );
          }
        );

        this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Enregistrement effectué'});
        setTimeout(() => {
          this.loadPisteList();
          this.display = false;
        }, 1700);
      }
    } else {
      this.http.get(apiPiste + this.idPiste).subscribe(
        res => {
          let piste = res;
          if (piste['nom'] != this.codePiste) {
            piste['nom'] = this.codePiste;
            piste['idSession'] = this.sessionService.getItem('currentUser')['idSession'];
            this.http.put(apiPiste, piste['id'], piste).subscribe(
              resu => {
              }
            );
          }
          this.http.get(apiPrixPiste + 'actuel/' + piste['id']).subscribe(
            resul => {
              let prixPiste = resul;
              if (prixPiste['montant'] != this.prixUnitairePiste) {
                prixPiste['dateFin'] = new Date();
                prixPiste["idSession"] = this.sessionService.getItem("currentUser")["idSession"];
                this.http.put(apiPrixPiste, prixPiste['id'], prixPiste).subscribe(
                  result => {
                  }
                );
                let nouveauPrixPiste = {
                  idPiste: piste['id'],
                  montant: this.prixUnitairePiste,
                  dateDebut: new Date(),
                  dateFin: null,
                  idSession: this.sessionService.getItem("currentUser")["idSession"]
                };
                this.http.post(apiPrixPiste, nouveauPrixPiste).subscribe(
                  resulta => {
                  }
                );
              }
            }
          );
        }
      );

      this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Enregistrement effectué'});
      setTimeout(() => {
        this.loadPisteList();
        this.display = false;
      }, 1700);
    }
  }

  reset() {
    this.codePiste = '';
    this.prixUnitairePiste = '';
  }

  closeHistoriqueDialog() {
    this.historiqueDisplay = false;
    this.codePiste = '';
  }

  getHistoriquePrix(piste: Piste) {
    this.codePiste = piste.nom;
    this.httpService.get(environment.apiPrixPiste + 'piste/' + piste.id).subscribe(
      (res: PrixPiste[]) => {
        this.prixPistes = res;
        for (let element of this.prixPistes) {
          if (element['dateFin'] == null) {
            this.prixActuel = element['montant'];
            this.dateDebutPrixActuel = element['dateDebut'];
            this.prixPistes = this.prixPistes.filter(item => item !== element);
          }
        }
      }
    );
    this.historiqueDisplay = true;
  }

}
