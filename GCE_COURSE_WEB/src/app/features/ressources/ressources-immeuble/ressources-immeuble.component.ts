import { MessageService } from 'primeng/api';
import { ConfirmationService } from 'primeng';
import { NombreLitsHistorique } from './../../../core/models/nombre-lits-historique.model';
import { Chambre } from './../../../core/models/chambre.model';
import { Immeuble } from './../../../core/models/immeuble.model';
import { SessionService } from './../../../core/services/session.service';
import { element } from 'protractor';
import { HttpService } from './../../../core/services/http.service';
import { environment } from 'src/environments/environment';
import { Component, OnInit } from '@angular/core';
import { Lit } from 'src/app/core/models/lit.model';
import { PrixLit } from 'src/app/core/models/prix-lit.model';

let apiImmeuble = environment.apiImmeuble;
let apiChambre = environment.apiChambre;
let apiLit = environment.apiLit;
let apiPrixLit = environment.apiPrixLit;
let apiNombreLitsHistorique = environment.apiNombreLitsHistorique;

@Component({
  selector: 'app-ressources-immeuble',
  templateUrl: './ressources-immeuble.component.html'
})
export class RessourcesImmeubleComponent implements OnInit {

  optionsActivation: any[];
  litDisableBoolean: boolean;
  immeubleDisableBoolean: boolean;
  chambreDisableBoolean: boolean;
  litDisable: any;
  chambreDisable: any;
  immeubleDisable: any;

  litOpened: Lit = new Lit();
  montant: any;
  changementPrixDisplay: boolean = false;
  typePrixList: any[];
  typePrixDropdown: any;

  immeubles: any[];
  chambres: any[];
  lits: any[];
  prixLits: any[];
  nombreLits: any[];

  idLit: any;
  codeLit: any;
  prixUnitaireLit: any;
  prixForfaitaireLit: any;
  chambreOpened: any;
  immeubleOpened: any;
  actionAjouter: any = "Immeuble";

  idImmeuble: any;
  codeImmeuble: any;
  idChambre: any;
  codeChambre: any;
  nbrLitsChambre: any;
  prixParJourChambre: any;

  action: any;
  litDisplay: boolean = false;
  immeubleDisplay: boolean = false;
  chambreDisplay: boolean = false;
  tableDisplay: boolean = false;
  historiqueDisplay: boolean = false;
  historiqueNombreLitsDisplay: boolean = false;

  prixActuel: any;
  dateDebutPrixActuel: any;

  nombreActuel: any;
  dateDebutNombreActuel: any;

  codeChambreInput: any = '';
  nbrLitsChambreInput: any = '';
  codeImmeubleInput: any = '';
  prixUnitaireLitInput: any = '';
  prixForfaitaireLitInput: any = '';

  profilUtilisateurConnecte: any;

  constructor(private http: HttpService,
    private sessionService: SessionService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService) {
    this.profilUtilisateurConnecte = this.sessionService.getItem("codeProfil");
    this.optionsActivation = [
      { name: 'Activer', code: '1' },
      { name: 'Désactiver', code: '0' }
    ];
    this.typePrixList = [
      { name: "Forfaitaire", code: "F" },
      { name: "Unitaire", code: "U" }
    ];
    this.typePrixDropdown = this.typePrixList[0];
  }

  ngOnInit() {
    this.loadImmeublesList();
  }

  loadImmeublesList() {
    this.http.get(apiImmeuble + "site/" + this.sessionService.getItem("site")["nom"]).subscribe(
      (res: any[]) => {
        this.immeubles = res;
      }
    )
  }

  onImmeubleChoosen(event) {
    this.resetChambreInputs();
    this.chambreOpened = null;
    if (event.value != null) {
      this.tableDisplay = false;
      let immeuble = event.value;
      this.immeubleOpened = event.value;
      this.codeImmeubleInput = this.immeubleOpened["nom"];
      this.http.get(apiChambre + "immeuble/" + immeuble["id"]).subscribe(
        (resultat: any[]) => {
          this.chambres = resultat;
          this.actionAjouter = "Chambre";
        }
      );
    }
  }

  onChambreChoosen(event) {
    this.actionAjouter = "Lit";
    if (event.value != null) {
      this.tableDisplay = false;
      let chambre = event.value;
      this.chambreOpened = event.value;
      this.codeChambreInput = this.chambreOpened["nom"];
      this.http.get(apiLit + "chambre/" + chambre["id"]).subscribe(
        (res: any[]) => {
          this.lits = res;
          this.tableDisplay = true;
          this.nbrLitsChambreInput = this.lits.length;
        }
      );
      this.http.get(apiPrixLit + "actuel/unitaire/" + chambre["id"]).subscribe(res => {
        this.prixUnitaireLitInput = res["montant"];
      });
      this.http.get(apiPrixLit + "actuel/forfaitaire/" + chambre["id"]).subscribe(res => {
        this.prixForfaitaireLitInput = res["montant"];
      });
    }
  }

  editLit(lit: Lit) {
    this.action = "Modifier";
    this.litOpened = lit;
    this.codeLit = lit["nom"];
    this.prixUnitaireLit = lit["montant"];
    this.idLit = lit["id"];
    if (lit["isActif"] == true) {
      this.litDisable = { name: 'Activer', code: '1' };
    } else {
      this.litDisable = { name: 'Désactiver', code: '0' };
    }
    this.litDisplay = true;
  }

  deleteLit(lit: Lit) {
    this.confirmationService.confirm({
      message: 'Etes-vous sûr de vouloir supprimer ce lit ?',
      accept: () => {
        this.http.deleteWithIdSession(apiLit, lit["id"], this.sessionService.getItem("currentUser")["idSession"]).subscribe(res => {
          this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Lit supprimé' });
          this.nbrLitsChambreInput = this.nbrLitsChambreInput - 1;
          for (let element of this.lits) {
            if (lit == element) {
              this.lits = this.lits.filter(item => item !== element);
              break;
            }
          }
        }, error => {
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Il n'est pas possible de supprimer un lit lié à une réservation." });
        });
      }
    });
  }

  getHistoriquePrix(lit: Lit) {
    this.http.get(apiPrixLit + "lit/" + lit["id"]).subscribe(
      (res: PrixLit[]) => {
        this.prixLits = res;
        for (let element of this.prixLits) {
          if (element["dateFin"] == null) {
            this.prixActuel = element["montant"];
            this.dateDebutPrixActuel = element["dateDebut"];
            this.prixLits = this.prixLits.filter(item => item !== element);
            break;
          }
        }
      }
    )
    setTimeout(() => {
      this.historiqueDisplay = true;
    }, 350);
  }

  closeHistoriqueDialog() {
    this.historiqueDisplay = false;
  }

  closeLitDialog() {
    this.litDisplay = false;
  }

  closeNombreLitsHistoriqueDialog() {
    this.historiqueNombreLitsDisplay = false;
  }

  reset() {
    this.codeLit = "";
    this.prixUnitaireLit = 0;
  }

  saveLit() {
    if (this.action == "Modifier") {
      //Modifier un lit
      this.litOpened["nom"] = this.codeLit;
      this.litOpened["idSession"] = this.sessionService.getItem("currentUser")["idSession"];
      this.litOpened["isActif"] = this.litDisable.code == 1 ? true : false;
      this.http.put(apiLit, this.litOpened["id"], this.litOpened).subscribe(res => {
        this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Lit modifié avec succès' });
        this.http.get(apiLit + "chambre/" + this.chambreOpened["id"]).subscribe(
          (res: any[]) => {
            this.lits = res;
            this.nbrLitsChambreInput = this.lits.length;
            setTimeout(() => {
              this.litDisplay = false;
            }, 1200);
          }
        );
      }, error => {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Le nom de ce lit existe déjà." });
      });

    } else {
      //Ajouter un lit
      let params = {
        idChambre: this.chambreOpened["id"],
        idSession: this.sessionService.getItem("currentUser")["idSession"]
      };
      console.info("params : ", params);
      this.http.get(apiLit + "ajouter/" + this.chambreOpened["id"] + "/session/" + this.sessionService.getItem("currentUser")["idSession"]).subscribe(res => {
        this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Lit ajouté avec succès' });
        this.http.get(apiLit + "chambre/" + this.chambreOpened["id"]).subscribe(
          (res: any[]) => {
            this.lits = res;
            this.nbrLitsChambreInput = this.lits.length;
          }
        );
      }, error => {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Le nom de ce lit existe déjà." });
      });
    }
  }

  addImmeuble() {
    this.action = "Ajouter";
    this.codeImmeuble = "";
    this.immeubleDisplay = true;
  }

  editImmeuble(immeuble: Immeuble) {
    this.action = 'Modifier';
    this.codeImmeuble = immeuble["nom"];
    this.idImmeuble = immeuble["id"];
    if (immeuble["isActif"] == true) {
      this.immeubleDisable = { name: 'Activer', code: '1' };
    } else {
      this.immeubleDisable = { name: 'Désactiver', code: '0' };
    }
    this.immeubleDisplay = true;
  }

  deleteImmeuble(immeuble: Immeuble) {
    this.confirmationService.confirm({
      message: 'Etes-vous sûr de vouloir supprimer ce bâtiment ?',
      accept: () => {
        this.http.delete(apiImmeuble, immeuble["id"]).subscribe(res => {
          this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Bâtiment supprimé' });
          setTimeout(() => {
            this.loadImmeublesList();
            this.chambres = [];
            this.chambreOpened = '';
            this.immeubleOpened = '';
            this.actionAjouter = 'Immeuble';
          }, 1000);
        }, error => {
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Il n'est pas possible de supprimer un bâtiment ayant des chambres ou lié à une réservation." });
        });
      }
    });
  }

  saveImmeuble() {
    if (this.action == "Ajouter") {
      let duplicatedName = false;
      for (let element of this.immeubles) {
        if (element["nom"] == this.codeImmeuble) {
          duplicatedName = true;
          break;
        }
      }
      if (duplicatedName == true) {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Le nom existe déjà" });
      } else {
        let immeuble = {
          nom: this.codeImmeuble,
          site: this.sessionService.getItem("site"),
          isActif: true,
          idSession: this.sessionService.getItem("currentUser")["idSession"]
        }
        this.http.post(apiImmeuble, immeuble).subscribe(
          res => {
            this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Bâtiment ajouté' });
            this.loadImmeublesList();
            setTimeout(() => {
              this.immeubleDisplay = false;
            }, 900);
          }
        )
      }
    } else {
      //Modifier Immeuble
      this.http.get(apiImmeuble + this.idImmeuble).subscribe(
        res => {
          let immeuble = res;
          if (immeuble["nom"] != this.codeImmeuble) {
            immeuble["nom"] = this.codeImmeuble;
            immeuble["idSession"] = this.sessionService.getItem("currentUser")["idSession"];
            this.http.put("http://localhost:8085/gce-course/immeubles", immeuble["id"], immeuble).subscribe(
              resu => {
                //Modification du nom
                this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Enregistrement effectué' });
                this.loadImmeublesList();
                setTimeout(() => {
                  this.immeubleDisplay = false;
                }, 900);
              }
            )
          }
          if (this.immeubleDisable["code"] == 0) {
            this.immeubleDisableBoolean = false;
          } else {
            this.immeubleDisableBoolean = true;
          }
          if (immeuble["isActif"] != this.immeubleDisableBoolean) {
            immeuble["isActif"] = this.immeubleDisableBoolean;
            this.http.put(apiImmeuble, immeuble["id"], immeuble).subscribe(
              resu => {
                //Modification du statut
                this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Enregistrement effectué' });
              }
            )
          }
        }
      );
    }
    setTimeout(() => {
      this.resetChambreInputs();
      this.loadImmeublesList();
      this.immeubleOpened = null;
      this.chambreOpened = null;
      this.actionAjouter = "Immeuble";
      this.immeubleDisplay = false;
    }, 1600);
  }

  addChambre() {
    this.action = "Ajouter";
    this.codeChambre = "";
    this.nbrLitsChambre = 0;
    this.prixParJourChambre = 0;
    this.chambreDisplay = true;
  }

  editChambre(chambre: Chambre) {
    this.action = 'Modifier';
    this.codeChambre = chambre["nom"];
    this.idChambre = chambre["id"];
    if (chambre["isActif"] == true) {
      this.chambreDisable = { name: 'Activer', code: '1' };
    } else {
      this.chambreDisable = { name: 'Désactiver', code: '0' };
    }
    this.http.get(apiPrixLit + "actuel/unitaire/" + this.chambreOpened["id"]).subscribe(res => {
      this.prixUnitaireLit = res["montant"];
    });
    this.http.get(apiPrixLit + "actuel/forfaitaire/" + this.chambreOpened["id"]).subscribe(res => {
      this.prixForfaitaireLit = res["montant"];
    });
    setTimeout(() => {
      this.chambreDisplay = true;
    }, 300);

  }

  deleteChambre(chambre: Chambre) {

    this.confirmationService.confirm({
      message: 'Etes-vous sûr de vouloir supprimer cette chambre ?',
      accept: () => {
        this.http.delete(apiChambre, chambre["id"]).subscribe(res => {
          this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Chambre supprimée' });
          this.http.get(apiChambre + "immeuble/" + this.immeubleOpened["id"]).subscribe(
            (resultat: any[]) => {
              this.chambres = resultat;
              setTimeout(() => {
                this.actionAjouter = "Chambre";
                this.codeChambreInput = "";
                this.nbrLitsChambreInput = "";
                this.chambreOpened = null;
                this.tableDisplay = false;
              }, 1000);
            }
          );
        }, error => {
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Il n'est pas possible de supprimer une chambre ayant des lits ou liée à une réservation." });
        });
      }
    });
  }

  saveChambre() {
    if (this.action == "Ajouter") {
      let chambre = {
        nom: this.codeChambre,
        immeuble: this.immeubleOpened,
        nombreLits: this.nbrLitsChambre,
        isActif: true,
        prixUnitaire: this.prixUnitaireLit,
        prixForfaitaire: this.prixForfaitaireLit,
        idSession: this.sessionService.getItem("currentUser")["idSession"]
      }

      this.http.post(apiChambre, chambre).subscribe(res => {
        this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Chambre ajoutée' });
        this.http.get(apiChambre + "immeuble/" + this.immeubleOpened["id"]).subscribe(
          (resultat: any[]) => {
            this.chambres = resultat;
          }
        );
        setTimeout(() => {
          this.resetChambreInputs();
          this.chambreOpened = null;
          this.actionAjouter = "Chambre";
          this.chambreDisplay = false;

        }, 1300);
      }, error => {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Le nom de cette chambre existe déjà." });
      });
    } else {

      this.chambreOpened["nom"] = this.codeChambre;
      this.chambreOpened["isActif"] = this.chambreDisable.code == 1 ? true : false;
      this.chambreOpened["prixUnitaire"] = this.prixUnitaireLit;
      this.chambreOpened["prixForfaitaire"] = this.prixForfaitaireLit;
      this.chambreOpened["idSession"] = this.sessionService.getItem("currentUser")["idSession"];
      this.http.put(apiChambre, this.chambreOpened["id"], this.chambreOpened).subscribe(res => {
        this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Chambre modifiée avec succès' });
        this.http.get(apiLit + "chambre/" + this.chambreOpened["id"]).subscribe(
          (res: any[]) => {
            this.lits = res;
            this.nbrLitsChambreInput = this.lits.length;
          }
        );
        this.http.get(apiPrixLit + "actuel/unitaire/" + this.chambreOpened["id"]).subscribe(res => {
          this.prixUnitaireLitInput = res["montant"];
        });
        this.http.get(apiPrixLit + "actuel/forfaitaire/" + this.chambreOpened["id"]).subscribe(res => {
          this.prixForfaitaireLitInput = res["montant"];
        });
        setTimeout(() => {
          this.chambreDisplay = false;
        }, 1200);
      }, error => {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Le nom de cette chambre existe déjà." });
      });
    }
  }

  addLit() {
    this.action = "Ajouter";
    this.codeLit = "";
    this.prixUnitaireLit = 0;
    this.litDisplay = true;
  }

  getHistoriqueChangementLits() {
    this.http.get(apiNombreLitsHistorique + "chambre/" + this.chambreOpened["id"]).subscribe(
      (res: NombreLitsHistorique[]) => {
        this.nombreLits = res;
        for (let element of this.nombreLits) {
          if (element["dateFin"] == null) {
            this.nombreActuel = element["nombre"];
            this.dateDebutNombreActuel = element["dateDebut"];
            this.nombreLits = this.nombreLits.filter(item => item !== element);
            break;
          }
        }
      }
    )
    setTimeout(() => {
      this.historiqueNombreLitsDisplay = true;
    }, 350);
  }

  getHistoriquePrixUnitaire() {
    this.http.get(apiPrixLit + "unitaire/" + this.chambreOpened["id"]).subscribe((res: any[]) => {
      this.prixLits = res;
      this.prixLits = this.prixLits.filter(element => element.dateFin != null);
    });
    this.http.get(apiPrixLit + "actuel/unitaire/" + this.chambreOpened["id"]).subscribe(res => {
      this.prixActuel = res["montant"];
      this.dateDebutPrixActuel = res["dateDebut"];
    });
    setTimeout(() => {
      this.historiqueDisplay = true;
    }, 300);
  }

  getHistoriquePrixForfaitaire() {
    this.http.get(apiPrixLit + "forfaitaire/" + this.chambreOpened["id"]).subscribe((res: any[]) => {
      this.prixLits = res;
      this.prixLits = this.prixLits.filter(element => element.dateFin != null);
    });
    this.http.get(apiPrixLit + "actuel/forfaitaire/" + this.chambreOpened["id"]).subscribe(res => {
      this.prixActuel = res["montant"];
      this.dateDebutPrixActuel = res["dateDebut"];
    });
    setTimeout(() => {
      this.historiqueDisplay = true;
    }, 300);
  }

  resetChambreInputs() {
    this.codeChambreInput = '';
    this.nbrLitsChambreInput = '';
  }

  closeChambreDialog() {
    this.chambreDisplay = false;
  }

  changerPrixLits() {
    this.changementPrixDisplay = true;
  }

  closeChangementPrixDialog() {
    this.changementPrixDisplay = false;
    this.montant = "";
  }

  savePrix() {
    this.http.get(apiPrixLit + "changerPrix/" + this.montant + "/typePrix/" + this.typePrixDropdown.code).subscribe(res => {
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Changement effectué' });
      setTimeout(() => {
        this.loadImmeublesList();
        this.changementPrixDisplay = false;
        this.montant = "";
      }, 500);
    });
  }

}
