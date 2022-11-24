import { Ecurie } from './../../../core/models/ecurie.model';
import { ConfirmationService, MessageService } from 'primeng/api';
import { SessionService } from './../../../core/services/session.service';
import { PrixBox } from './../../../core/models/prix-box.model';
import { element } from 'protractor';
import { HttpService } from './../../../core/services/http.service';
import { Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Box } from 'src/app/core/models/box.model';

let apiPiste = environment.apiPiste;
let apiEcurie = environment.apiEcurie;
let apiBox = environment.apiBox;
let apiPrixBox = environment.apiPrixBox;

@Component({
  selector: 'app-ressources-ecurie',
  templateUrl: './ressources-ecurie.component.html'
})
export class RessourcesEcurieComponent implements OnInit {

  typePrixList: any[];
  typePrixDropdown: any;
  montant: any;
  ecuries: any[];
  boxs: any[];
  prixBoxs: PrixBox[];

  ecurie: any;

  optionsActivation: any[];
  boxDisable: any;
  ecurieDisable: any;
  ecurieDisableBoolean: boolean;
  boxDisableBoolean: boolean;

  prixActuel: any;
  dateDebutPrixActuel: any;
  action: any;
  actionAjouter: any = "Ecurie";
  codeBox: any;
  prixUnitaireBox: any;
  prixForfaitaireBox: any;
  codeEcurie: any;
  nombreBoxs: any = 0;
  boxOpened: any;
  ecurieOpened: any;

  boxDisplay: boolean = false;
  ecurieDisplay: boolean = false;
  tableDisplay: boolean = false;
  loading: boolean = false;
  historiqueDisplay: boolean = false;
  changementPrixDisplay: boolean = false;

  profilUtilisateurConnecte: any;

  constructor(private http: HttpService,
    private sessionService: SessionService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService) {
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

    this.loadEcuriesList();
  }

  loadEcuriesList() {
    this.http.get(apiEcurie + "site/" + this.sessionService.getItem("site")["nom"]).subscribe(
      (res: any[]) => {
        this.ecuries = res;
      }
    )
  }

  onEcurieChoosen(event) {
    if (event.value != null) {
      this.loading = true;
      this.tableDisplay = false;
      this.ecurie = event.value;
      this.http.get(apiBox + "ecurie/" + this.ecurie["id"]).subscribe(
        (res: any[]) => {
          this.boxs = res;
          for (let element of this.boxs) {
            this.http.get(apiPrixBox + "actuels/" + element["id"]).subscribe(
              (resultat: any[]) => {
                for (let element2 of resultat) {
                  if (element2["typePrix"] == "U") {
                    element["prixUnitaire"] = element2["montant"];
                  }
                  if (element2["typePrix"] == "F") {
                    element["prixForfaitaire"] = element2["montant"];
                  }
                }
              }
            )
          }
        }
      );
      setTimeout(() => {
        this.loading = false;
        this.tableDisplay = true;
        this.actionAjouter = "Box";
      }, 900);
    } else {
      this.ecurie = null;
      this.tableDisplay = false;
      this.actionAjouter = "Ecurie";
    }

  }

  getHistoriquePrixUnitaire(box: Box) {
    this.http.get(apiPrixBox + "box/unitaire/" + box["id"]).subscribe(
      (res: PrixBox[]) => {
        this.prixBoxs = res;
        for (let element of this.prixBoxs) {
          if (element["dateFin"] == null) {
            this.prixActuel = element["montant"];
            this.dateDebutPrixActuel = element["dateDebut"];
            this.prixBoxs = this.prixBoxs.filter(item => item !== element);
            break;
          }
        }
      }
    )
    setTimeout(() => {
      this.historiqueDisplay = true;
    }, 350);
  }

  getHistoriquePrixForfaitaire(box: Box) {
    this.http.get(apiPrixBox + "box/forfaitaire/" + box["id"]).subscribe(
      (res: PrixBox[]) => {
        this.prixBoxs = res;
        for (let element of this.prixBoxs) {
          if (element["dateFin"] == null) {
            this.prixActuel = element["montant"];
            this.dateDebutPrixActuel = element["dateDebut"];
            this.prixBoxs = this.prixBoxs.filter(item => item !== element);
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
    this.prixBoxs = [];
    this.prixActuel = '';
    this.dateDebutPrixActuel = '';
    this.historiqueDisplay = false;
  }

  editItem(box: Box) {
    this.boxOpened = box;
    this.action = "Modifier";
    this.codeBox = box.nom;
    this.prixUnitaireBox = box["prixUnitaire"];
    this.prixForfaitaireBox = box["prixForfaitaire"];
    if (box["isActif"] == true) {
      this.boxDisable = { name: 'Activer', code: '1' };
    } else {
      this.boxDisable = { name: 'Désactiver', code: '0' };
    }
    this.boxDisplay = true;
  }

  editEcurie(ecurie: Ecurie) {
    this.ecurieOpened = ecurie;
    this.action = "Modifier";
    this.codeEcurie = ecurie.nom;
    if (ecurie["isActif"] == true) {
      this.ecurieDisable = { name: 'Activer', code: '1' };
    } else {
      this.ecurieDisable = { name: 'Désactiver', code: '0' };
    }
    this.ecurieDisplay = true;
  }

  deleteEcurie(ecurie: Ecurie) {
    this.confirmationService.confirm({
      message: 'Etes-vous sûr de vouloir supprimer cette écurie ?',
      accept: () => {
        this.http.delete(apiEcurie, ecurie["id"]).subscribe(res => {
          this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Ecurie supprimée' });
          setTimeout(() => {
            this.ecurie = null;
            this.loadEcuriesList();
            this.tableDisplay = false;
            this.actionAjouter = "Ecurie";
          }, 500);
        }, error => {
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Il n'est pas possible de supprimer une écurie ayant des boxs ou liée à une réservation." });
        });
      }
    });

  }

  addItem() {
    this.action = 'Ajouter';
    this.codeBox = '';
    this.prixUnitaireBox = '';
    this.boxDisplay = true;
  }

  addEcurie() {
    this.action = 'Ajouter';
    this.codeEcurie = "";
    this.ecurieDisplay = true;
  }

  saveEcurie() {
    if (this.action == "Ajouter") {
      let duplicatedName = false;
      for (let element of this.ecuries) {
        if (this.codeEcurie == element["nom"]) {
          duplicatedName = true;
          break;
        }
      }
      if (duplicatedName == true) {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: 'Le nom existe déjà.' });
      } else {
        let ecurie = {
          nom: this.codeEcurie,
          site: this.sessionService.getItem("site"),
          isActif: true,
          idSession: this.sessionService.getItem("currentUser")["idSession"]
        };
        this.http.post(apiEcurie, ecurie).subscribe(
          res => {
            this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Ecurie ajoutée.' });
            setTimeout(() => {
              this.loadEcuriesList();
              this.ecurieDisplay = false;
              this.ecurie = null;
              this.tableDisplay = false;
              this.actionAjouter = "Ecurie";
            }, 900);
          }
        )
      }
    } else {
      this.ecurieOpened["nom"] = this.codeEcurie;
      this.http.put(apiEcurie, this.ecurieOpened["id"], this.ecurieOpened).subscribe(
        res => {
          this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Enregistrement effectué.' });
          setTimeout(() => {
            this.loadEcuriesList();
            this.ecurieDisplay = false;
          }, 900);
        }, error => {
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Le nom de cette écurie existe déjà." });
        }
      )
    }

  }

  addBox() {
    this.action = 'Ajouter';
    this.codeBox = '';
    this.prixUnitaireBox = '';
    this.boxDisplay = true;
  }

  changerPrixBoxes() {
    this.changementPrixDisplay = true;
  }

  closeChangementPrixDialog() {
    this.changementPrixDisplay = false;
    this.montant = "";
  }

  savePrix() {
    this.http.get(apiPrixBox + "changerPrix/" + this.montant + "/typePrix/" + this.typePrixDropdown.code).subscribe(res => {
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Changement effectué' });
      setTimeout(() => {
        this.http.get(apiBox + "ecurie/" + this.ecurie["id"]).subscribe(
          (res: any[]) => {
            this.boxs = res;
            for (let element of this.boxs) {
              this.http.get(apiPrixBox + "actuels/" + element["id"]).subscribe(
                (resultat: any[]) => {
                  for (let element2 of resultat) {
                    if (element2["typePrix"] == "U") {
                      element["prixUnitaire"] = element2["montant"];
                    }
                    if (element2["typePrix"] == "F") {
                      element["prixForfaitaire"] = element2["montant"];
                    }
                  }
                }
              )
            }
          }
        );
        this.reset();
        this.montant = "";
        this.changementPrixDisplay = false;
      }, 500);
    });
  }

  ajouterBox() {
    this.http.post(apiBox + "ajouter", this.ecurie["id"]).subscribe(res => {
      this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Box ajouté avec succès' });
      setTimeout(() => {
        this.http.get(apiBox + "ecurie/" + this.ecurie["id"]).subscribe(
          (res: any[]) => {
            this.boxs = res;
            for (let element of this.boxs) {
              this.http.get(apiPrixBox + "actuels/" + element["id"]).subscribe(
                (resultat: any[]) => {
                  for (let element2 of resultat) {
                    if (element2["typePrix"] == "U") {
                      element["prixUnitaire"] = element2["montant"];
                    }
                    if (element2["typePrix"] == "F") {
                      element["prixForfaitaire"] = element2["montant"];
                    }
                  }
                }
              )
            }
          }
        );
        this.reset();
        this.boxDisplay = false;
      }, 500);
    }, error => {
      this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Le nom de ce lit existe déjà." });
    });
  }

  saveBox() {
    if (this.action == 'Ajouter') {
      this.http.post(apiBox + "ajouter", this.ecurieOpened["id"]).subscribe(res => {
        this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Box ajouté avec succès' });
        setTimeout(() => {
          this.http.get(apiBox + "ecurie/" + this.ecurie["id"]).subscribe(
            (res: any[]) => {
              this.boxs = res;
              for (let element of this.boxs) {
                this.http.get(apiPrixBox + "actuels/" + element["id"]).subscribe(
                  (resultat: any[]) => {
                    for (let element2 of resultat) {
                      if (element2["typePrix"] == "U") {
                        element["prixUnitaire"] = element2["montant"];
                      }
                      if (element2["typePrix"] == "F") {
                        element["prixForfaitaire"] = element2["montant"];
                      }
                    }
                  }
                )
              }
            }
          );
          this.reset();
          this.boxDisplay = false;
        }, 1700);
      }, error => {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Le nom de ce lit existe déjà." });
      });









      // let box = {
      //   nom: this.codeBox,
      //   ecurie: this.ecurie,
      //   isActif: true,
      //   prixUnitaire: this.prixUnitaireBox,
      //   prixForfaitaire: this.prixForfaitaireBox,
      //   idSession: this.sessionService.getItem("currentUser")["idSession"]
      // }
      // this.http.post(apiBox, box).subscribe(
      //   (res: Box) => {
      //     this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Box ajouté.' });
      //     setTimeout(() => {
      //       this.http.get(apiBox + "ecurie/" + this.ecurie["id"]).subscribe(
      //         (res: any[]) => {
      //           this.boxs = res;
      //           for (let element of this.boxs) {
      //             this.http.get(apiPrixBox + "actuels/" + element["id"]).subscribe(
      //               (resultat: any[]) => {
      //                 for (let element2 of resultat) {
      //                   if (element2["typePrix"] == "U") {
      //                     element["prixUnitaire"] = element2["montant"];
      //                   }
      //                   if (element2["typePrix"] == "F") {
      //                     element["prixForfaitaire"] = element2["montant"];
      //                   }
      //                 }
      //               }
      //             )
      //           }
      //         }
      //       );
      //       this.reset();
      //       this.boxDisplay = false;
      //     }, 1700);
      //   }, error => {
      //     this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Le nom de ce Box existe déjà." });
      //   }
      // );














    } else {
      let box = {
        id: this.boxOpened["id"],
        nom: this.codeBox,
        isActif: this.boxDisable.code == 0 ? false : true,
        prixUnitaire: this.prixUnitaireBox,
        prixForfaitaire: this.prixForfaitaireBox,
        idSession: this.sessionService.getItem("currentUser")["idSession"]
      };
      this.http.put(apiBox, this.boxOpened["id"], box).subscribe(res => {
        this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Enregistrement effectué.' });
        setTimeout(() => {
          this.http.get(apiBox + "ecurie/" + this.ecurie["id"]).subscribe(
            (res: any[]) => {
              this.boxs = res;
              for (let element of this.boxs) {
                this.http.get(apiPrixBox + "actuels/" + element["id"]).subscribe(
                  (resultat: any[]) => {
                    for (let element2 of resultat) {
                      if (element2["typePrix"] == "U") {
                        element["prixUnitaire"] = element2["montant"];
                      }
                      if (element2["typePrix"] == "F") {
                        element["prixForfaitaire"] = element2["montant"];
                      }
                    }
                  }
                )
              }
            }
          );
          this.reset();
          this.boxDisplay = false;
        }, 1700);
      }, error => {
        this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Le nom de ce Box existe déjà." });
      });
    }
  }

  saveItem() {

  }

  reset() {
    this.codeBox = '';
    this.prixUnitaireBox = '';
    this.prixForfaitaireBox = '';
    this.codeEcurie = '';
  }

  deleteItem(box: Box) {
    this.confirmationService.confirm({
      message: 'Etes-vous sûr de vouloir supprimer ce box ?',
      accept: () => {
        this.http.delete(apiBox, box["id"]).subscribe(res => {
          this.messageService.add({ severity: 'success', summary: 'Confirmation', detail: 'Box supprimé' });
          this.boxs = this.boxs.filter(obj => obj !== box);
        }, error => {
          this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Il n'est pas possible de supprimer un box lié à une réservation." });
        });
      }
    });
  }

  closeBoxDialog() {
    this.boxDisplay = false;
    this.reset();
  }
}
