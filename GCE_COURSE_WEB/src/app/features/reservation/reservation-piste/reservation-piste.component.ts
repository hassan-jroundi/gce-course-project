import { Piste } from 'src/app/core/models/piste.model';
import { element } from 'protractor';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { ConfirmationService } from 'primeng';
import { Subscription } from 'rxjs';
import { BookingDto } from 'src/app/core/models/bookingdto';
import { Box } from 'src/app/core/models/box.model';
import { RaisonSociale } from 'src/app/core/models/raison-sociale.model';
import { RoomDto } from 'src/app/core/models/roomdto';
import { HttpService } from 'src/app/core/services/http.service';
import { ReservationService } from 'src/app/core/services/reservation-service';
import { SessionService } from 'src/app/core/services/session.service';
import { ChangeFilterReservationArg } from 'src/app/shared/scheduler/changefilterreservationarg';
import { ChangeReservationArg } from 'src/app/shared/scheduler/changereservationarg';
import { ReservationArg } from 'src/app/shared/scheduler/reservationargs';
import { environment } from 'src/environments/environment';
import {MessageService} from 'primeng/api';

let apiBox = environment.apiBox;
let apiReservation = environment.apiReservation;
let apiEcurie = environment.apiEcurie;
let apiPersonne = environment.apiPersonne;
let apiChevalBox = environment.apiChevalBox;
let apiChambre = environment.apiChambre;
let apiChevalPiste = environment.apiChevalPiste;
let apiPiste = environment.apiPiste;

@Component({
  selector: 'app-reservation-piste',
  templateUrl: './reservation-piste.component.html'
})
export class ReservationPisteComponent implements OnInit {

  typePrixDropdown: any;
  typePrixList: any[];
  enCours: boolean;
  today: Date = new Date();
  reservationId: any;
  selectedReservations: any[] = [];
  nombrePlaces: any = 0;
  reservations: any[] = [];
  pisteId: any;
  pisteNom: any;
  ecuries: any[];
  typeReservation: string;
  year: number;
  month: number;
  day: number;
  currentsearch: ChangeReservationArg;
  sub: Subscription;
  rooms: RoomDto[];
  boxs: Box[];
  boxList: any[] = [];
  bookings: BookingDto[];
  display: boolean = false;
  selectionDisplay: boolean = false;
  personneSelectionDisplay: boolean = false;
  scheduleDisplay: boolean = true;
  editReservationDisplay: boolean = false;
  pisteDropdown: any;
  heureDebut: any = 6;
  heureFin: any = 6;
  heureDebutOld: any = 0;
  heureFinOld: any = 0;
  minuteDebut: any = 0;
  minuteFin: any = 30;
  minuteDebutOld: any = 0;
  minuteFinOld: any = 0;
  heureDebutEdit: any = 0;
  heureFinEdit: any = 0;
  minuteDebutEdit: any = 0;
  minuteFinEdit: any = 0;

  raisonSocialeList: RaisonSociale[];
  nomSearch: any = '';
  nomSearchEdit: any = '';
  prenomSearch: any = '';
  prenomSearchEdit: any = '';
  cinSearch: any = '';
  cinSearchEdit: any = '';
  designationSearch: any = '';
  designationSearchEdit: any = '';
  raisonSocialeSearch: any = '';
  raisonSocialeSearchEdit: any = '';
  typeRaisonSocialeSearch: any;
  choixRaisonSociale: any;
  raisonSocialeCatched: RaisonSociale;
  action: any = '';
  ecurie: any;
  dateDebut: Date;
  dateFin: Date;
  date: Date;
  dateEdit: Date;

  nomChevalSearch: any = '';
  numeroEsrimaSearch: any = '';
  nomChevalSearchEdit: any = '';
  numeroEsrimaSearchEdit: any = '';

  chevals: any[];
  personneAFacturers: any[];
  ecurieDropdown: any;
  boxDropdown: any;
  personneAFacturer: any;
  cheval: any;

  bookingEdit: any;
  personneAFacturerPersonneId: any;
  chevalId: any;
  invalidDates: Date[];

  chevalBoxs: any[];


  data: any;
  chartOptions: any;

  profilUtilisateurConnecte: any;

  constructor(
    private messageService: MessageService,
    private dialog: MatDialog,
    private confirmationService: ConfirmationService,
    private service: ReservationService,
    private cd: ChangeDetectorRef,
    private http: HttpService,
    private sessionService: SessionService
  ) {
    this.profilUtilisateurConnecte = this.sessionService.getItem("codeProfil");
    this.typeReservation = "piste";
    const d = new Date();
    this.year = d.getFullYear();
    this.month = d.getMonth() + 1;
    this.day = d.getDate();
    this.rooms = [];
    this.bookings = [];
    this.boxs = [];
    this.invalidDates = [];
    this.dateDebut = new Date();
    this.dateFin = new Date();
    // this.invalidDates.push(new Date());
    this.raisonSocialeList = [
      { name: 'Personne Physique', code: 'P' },
      { name: 'Personne Morale', code: 'M' },
      { name: 'Association', code: 'A' }
    ];
    this.typePrixList = [
      { name: "Forfaitaire", code: "F" },
      { name: "Unitaire", code: "U" }
    ];
  }

  ngOnInit() {
    this.loadEcuriesList();
    // this.loadPistesList();
  }

  loadEcuriesList() {
    this.http.get(apiEcurie + "site/" + this.sessionService.getItem("site")["nom"]).subscribe(
      (res: any[]) => {
        this.ecuries = res;
      }
    )
  }

  loadPistesList() {
    this.http.get(apiPiste + "site/" + this.sessionService.getItem("site")["nom"]).subscribe(
      (res: any[]) => {
        this.pisteId = res[0]["id"];
      }
    )
  }

  onRaisonSocialeChange(event) {
    this.raisonSocialeCatched = event.value;
    this.choixRaisonSociale = this.raisonSocialeCatched?.code;
    this.resetSearchInputs();
  }

  onRaisonSocialeEditChange(event) {
    this.raisonSocialeCatched = event.value;
    this.choixRaisonSociale = this.raisonSocialeCatched?.code;
    this.resetEditSearchInputs();
  }

  resetSearchInputs() {
    this.nomSearch = '';
    this.prenomSearch = '';
    this.cinSearch = '';
    this.designationSearch = '';
    this.raisonSocialeSearch = '';
  }

  resetEditSearchInputs() {
    this.nomSearchEdit = '';
    this.prenomSearchEdit = '';
    this.cinSearchEdit = '';
    this.designationSearchEdit = '';
    this.raisonSocialeSearchEdit = '';
  }

  onReservationChanged(args: ChangeReservationArg) {
    this.http.get(apiPiste + "site/" + this.sessionService.getItem("site")["nom"]).subscribe(
      (res: any[]) => {
        this.pisteId = res[0]["id"];
        this.pisteNom = res[0]["nom"];
      }
    );
    this.bookings = [];
    this.currentsearch = args;
    this.rooms = this.service.getPisteHours();
    if (this.sub) {
      this.sub.unsubscribe();
      this.sub = undefined;
    }
    let params = {
      nomSite: this.sessionService.getItem("site")["nom"],
      idPiste: this.pisteId ? this.pisteId : 0,
      mois: (args.endDate.getUTCMonth()) + 1,
      annee: args.startDate.getUTCFullYear()
    }
    this.sub = this.http.search(apiReservation + "chevalPistes/criteria", params).subscribe(
      (res: any[]) => {
        for (let element of res) {
          let booking = new BookingDto();
          booking.bookingId = element["id"];
          // booking.roomId = element["chevalPistes"][0]["piste"]["id"];
          booking.startDate = new Date(element["chevalPistes"][0]["dateDebut"]);
          booking.endDate = new Date(element["chevalPistes"][0]["dateFin"]);
          booking.stayDay = element["chevalPistes"][0]["dureeHebergement"];
          switch (new Date(element["chevalPistes"][0]["dateDebut"]).getHours()) {
            case 6: {
              booking.roomId = 1;
              break;
            }
            case 7: {
              booking.roomId = 2;
              break;
            }
            case 8: {
              booking.roomId = 3;
              break;
            }
            case 9: {
              booking.roomId = 4;
              break;
            }
            case 10: {
              booking.roomId = 5;
              break;
            }
            case 11: {
              booking.roomId = 6;
              break;
            }
            case 12: {
              booking.roomId = 7;
              break;
            }
            default: {
              booking.roomId = 0;
              break;
            }
          }
          // booking.name = "Réservation : " + element["chevalPistes"][0]["dateDebut"] + " - " + element["chevalBoxs"][0]["dateFin"] + " - Cheval : " + element["chevalBoxs"][0]["cheval"]["nom"];
          this.bookings.push(booking);
        }

      }
    );
    this.cd.detectChanges();
    // this.sub = this.http.get(apiBox + "site/" + this.sessionService.getItem("site")["nom"]).subscribe((res: Box[]) => {
    //   for (let element of res) {
    //     let room = new RoomDto();
    //     room.roomId = element["id"];
    //     room.roomNumber = element["nom"];
    //     this.rooms.push(room);
    //   }
    //   this.http.get(apiReservation + "chevalBoxs").subscribe((res: any[]) => {
    //     let resultat = res;
    //     for (let element of res) {
    //       let booking = new BookingDto();
    //       booking.bookingId = element["id"];
    //       booking.roomId = element["chevalBoxs"][0]["box"]["id"];
    //       booking.boxName = element["chevalBoxs"][0]["box"]["nom"];
    //       booking.startDate = new Date(element["chevalBoxs"][0]["dateDebut"]);
    //       booking.endDate = new Date(element["chevalBoxs"][0]["dateFin"]);
    //       booking.stayDay = element["chevalBoxs"][0]["dureeHebergement"];
    //       booking.name = "Réservation : " + element["chevalBoxs"][0]["dateDebut"] + " - " + element["chevalBoxs"][0]["dateFin"] + " - Cheval : " + element["chevalBoxs"][0]["cheval"]["nom"];
    //       this.bookings.push(booking);

    //     }


    //   });
    //   this.cd.detectChanges();

    // });

  }

  onDayReservation(args: ReservationArg) {
    this.reservations = [];
    this.data = [];
    this.chartOptions = [];
    this.heureDebut = args.roomid;
    this.heureDebutOld = args.roomid;
    this.heureFin = args.roomid;
    this.minuteDebut = 0;
    this.minuteFin = 30;
    this.minuteDebutOld = this.minuteDebut;
    this.minuteFinOld = this.minuteFin;
    this.heureFinOld = args.roomid;
    let hour = args.roomid;
    if (this.today.getTime() >= args.date.getTime()) {
      this.date = this.today;
    } else {
      this.date = args.date;
    }
    let params = {
      date: args.date,
      heure: args.roomid,
      idPiste: this.pisteId
    }
    this.http.search(apiReservation + "chevalPistes/date", params).subscribe(
      (res: any[]) => {
        this.reservations = res;
        this.nombrePlaces = res.length;
        this.data = {
          labels: ['Occupées','Libres'],
          datasets: [
            {
              data: [res.length, (20 - res.length)],
              backgroundColor: [
                "rgb(80, 148, 183)",
                "rgb(240, 240, 240)"
              ],
              hoverBackgroundColor: [
                "rgb(80, 148, 183, 0.80)",
                "rgb(230, 230, 230)"
              ]
            }
          ]
        };
      }
    );

    this.action = "Ajouter";
    this.typePrixDropdown = this.typePrixList[0];

    setTimeout(() => {
      this.display = true;
    }, 1000);

    // this.http.get(apiChevalBox + "box/" + args["roomid"]).subscribe((res: any[]) => {
    //   this.chevalBoxs = res;
    //   for (let element of this.chevalBoxs) {
    //     if (!(args.booking.startDate.getTime() == new Date(element["dateDebut"]).getTime() && args.booking.endDate.getTime() == new Date(element["dateFin"]).getTime())) {
    //       let startTime = new Date(element["dateDebut"]).getTime();
    //       let endTime = new Date(element["dateFin"]).getTime();
    //       for (let loopTime = startTime; loopTime <= endTime; loopTime += 86400000) {
    //         var loopDay = new Date(loopTime);
    //         this.invalidDates.push(loopDay);
    //       }
    //     }
    //   }
    // });

    // this.http.get(apiBox + args["roomid"]).subscribe(res => {

    //   this.ecurieDropdown = res["ecurie"];
    //   this.http.get(apiBox + "ecurie/" + this.ecurieDropdown["id"]).subscribe(
    //     (resu: any[]) => {
    //       this.boxList = resu;
    //     }
    //   );
    //   this.boxDropdown = res;
    // })
    // if (args["booking"]["bookingId"] != 0) {
    //   this.action = "Modifier";
    //   this.bookingEdit = args;
    //   this.dateDebut = args["booking"]["startDate"];
    //   this.dateFin = args["booking"]["endDate"];
    //   this.http.get(apiChevalBox + "reservation/" + args["booking"]["bookingId"]).subscribe(res => {

    //     this.numeroEsrimaSearch = res["cheval"]["numeroEsrima"];
    //     this.nomChevalSearch = res["cheval"]["nom"];
    //     this.chevalId = res["cheval"]["id"];
    //     this.http.get(apiChevalBox + "cheval/" + this.chevalId).subscribe((result: any[]) => {
    //       for (let element of result) {
    //         let startTime = new Date(element["dateDebut"]).getTime();
    //         let endTime = new Date(element["dateFin"]).getTime();
    //         for (let loopTime = startTime; loopTime <= endTime; loopTime += 86400000) {
    //           var loopDay = new Date(loopTime);
    //           this.invalidDates.push(loopDay);
    //         }
    //       }
    //     });
    //   });
    //   this.http.get(apiReservation + args["booking"]["bookingId"]).subscribe(res => {
    //     this.personneAFacturer = res["personneAFacturer"];
    //     this.personneAFacturerPersonneId = res["personneAFacturer"]["personne"]["id"];
    //     if (res["personneAFacturer"]["personne"]["codeNaturePersonne"] == "P") {
    //       this.nomSearch = res["personneAFacturer"]["personne"]["nom"];
    //       this.prenomSearch = res["personneAFacturer"]["personne"]["prenom"];
    //       this.cinSearch = res["personneAFacturer"]["personne"]["numeroPieceIdentite"];
    //       this.typeRaisonSocialeSearch = this.raisonSocialeList[0];
    //       this.raisonSocialeCatched = this.typeRaisonSocialeSearch;
    //       this.choixRaisonSociale = this.raisonSocialeCatched?.code;
    //     }
    //     if (res["personneAFacturer"]["personne"]["codeNaturePersonne"] == "A") {
    //       this.designationSearch = res["personneAFacturer"]["personne"]["designation"];
    //       this.typeRaisonSocialeSearch = this.raisonSocialeList[2];
    //     }
    //     if (res["personneAFacturer"]["personne"]["codeNaturePersonne"] == "M") {
    //       this.raisonSocialeSearch = res["personneAFacturer"]["personne"]["raisonSociale"];
    //       this.typeRaisonSocialeSearch = this.raisonSocialeList[1];
    //     }
    //   });
    // } else {
    //   this.action = "Ajouter";
    //   let date = new Date(args["date"]);
    //   this.dateDebut = args["date"];
    //   this.dateFin = new Date(date.getTime() + (1000 * 60 * 60 * 24));
    // }
    // this.display = true;

  }

  onEcurieChoosen(event) {
    if (event.value != null) {
      this.ecurie = event.value;
      this.http.get(apiBox + "ecurie/" + this.ecurie["id"]).subscribe(
        (res: any[]) => {
          this.boxList = res;
        }
      );
    } else {
      this.boxList = [];
    }
  }

  onPisteReservationChanged(args: ChangeFilterReservationArg) {
    if (args.roomtype != 0) {
      this.pisteId = args.roomtype;
      // this.rooms = [];
      if (this.sub) {
        this.sub.unsubscribe();
        this.sub = undefined;
      }
      if (args.roomtype != undefined) {
        this.sub = this.http.get(apiReservation + "chevalPistes/" + args.roomtype).subscribe((res: any[]) => {
          let resultat = res;
          for (let element of res) {
            let booking = new BookingDto();
            booking.bookingId = element["id"];
            booking.roomId = element["chevalPistes"][0]["piste"]["id"];
            booking.boxName = element["chevalPistes"][0]["piste"]["nom"];
            booking.startDate = new Date(element["chevalPistes"][0]["dateDebut"]);
            booking.endDate = new Date(element["chevalPistes"][0]["dateFin"]);
            booking.stayDay = element["chevalPistes"][0]["dureeHebergement"];
            booking.name = "Réservation : " + element["chevalPistes"][0]["dateDebut"] + " - " + element["chevalPistes"][0]["dateFin"] + " - Cheval : " + element["chevalPistes"][0]["cheval"]["nom"];
            this.bookings.push(booking);
          }
          this.cd.detectChanges();

        });
      }
    } else {
      this.bookings = [];
      this.cd.detectChanges();
    }
  }

  doSearch() {
    if (this.nomSearch.length == 0 && this.prenomSearch.length == 0 && this.cinSearch.length == 0 && this.designationSearch.length == 0 && this.raisonSocialeSearch.length == 0) {
      this.messageService.add({severity: 'warn', summary: 'Attention', detail: 'Merci de saisir une valeur pour la recherche.'});
    } else {
      let params = {
        nom: this.nomSearch,
        prenom: this.prenomSearch,
        cin: this.cinSearch,
        designation: this.designationSearch,
        raisonSociale: this.raisonSocialeSearch
      }

      this.http.search(apiPersonne + 'search2', params).subscribe(
        (res: any[]) => {

          this.personneAFacturers = res;
          if (this.personneAFacturers.length == 1) {
            this.personneAFacturer = this.personneAFacturers[0];
            this.nomSearch = this.personneAFacturers[0]["nom"];
            this.prenomSearch = this.personneAFacturers[0]["prenom"];
            this.designationSearch = this.personneAFacturers[0]["designation"];
            this.raisonSocialeSearch = this.personneAFacturers[0]["raisonSociale"];
            this.cinSearch = this.personneAFacturers[0]["numeroPieceIdentite"];
            this.personneAFacturerPersonneId = this.personneAFacturers[0]["id"];
          } else if (this.personneAFacturers.length > 1) {
            this.personneSelectionDisplay = true;
          } else {
            this.messageService.add({severity: 'error', summary: 'Erreur', detail: 'Aucune personne trouvée !'});
          }
        }
      );
    }
  }

  doSearchEdit() {
    if (this.nomSearchEdit.length == 0 && this.prenomSearchEdit.length == 0 && this.cinSearchEdit.length == 0 && this.designationSearchEdit.length == 0 && this.raisonSocialeSearchEdit.length == 0) {
      this.messageService.add({severity: 'warn', summary: 'Attention', detail: 'Merci de saisir une valeur pour la recherche.'});
    } else {
      let params = {
        nom: this.nomSearchEdit,
        prenom: this.prenomSearchEdit,
        cin: this.cinSearchEdit,
        designation: this.designationSearchEdit,
        raisonSociale: this.raisonSocialeSearchEdit
      }

      this.http.search(apiPersonne + 'search2', params).subscribe(
        (res: any[]) => {

          this.personneAFacturers = res;
          if (this.personneAFacturers.length == 1) {
            this.personneAFacturer = this.personneAFacturers[0];
            this.nomSearchEdit = this.personneAFacturers[0]["nom"];
            this.prenomSearchEdit = this.personneAFacturers[0]["prenom"];
            this.designationSearchEdit = this.personneAFacturers[0]["designation"];
            this.raisonSocialeSearchEdit = this.personneAFacturers[0]["raisonSociale"];
            this.cinSearchEdit = this.personneAFacturers[0]["numeroPieceIdentite"];
            this.personneAFacturerPersonneId = this.personneAFacturers[0]["id"];
          } else if (this.personneAFacturers.length > 1) {
            this.personneSelectionDisplay = true;
          } else {
            this.messageService.add({severity: 'error', summary: 'Erreur', detail: 'Aucune personne trouvée !'});
          }
        }
      );
    }
  }

  doChevalSearch() {
    if (this.numeroEsrimaSearch == '' && this.nomChevalSearch == '') {
      this.messageService.add({severity: 'warn', summary: 'Attention', detail: 'Merci de saisir un N° ESRIMA ou Nom pour la recherche !'});
    } else {

      let params = {
        numeroEsrima: this.numeroEsrimaSearch != '' ? this.numeroEsrimaSearch : '',
        nomCheval: this.nomChevalSearch != '' ? this.nomChevalSearch : ''
      }

      this.http.search(environment.apiCheval + "search", params).subscribe(
        (res: any[]) => {
          this.chevals = res;
          if (this.chevals.length == 1) {
            this.cheval = this.chevals[0];
            this.nomChevalSearch = this.chevals[0]["nom"];
            this.numeroEsrimaSearch = this.chevals[0]["numeroEsrima"];
            this.chevalId = this.cheval[0]["id"];
          } else if (this.chevals.length > 1) {
            this.selectionDisplay = true;
          } else {
            this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Aucun cheval trouvé !"});
          }
        }
      );
    }
  }

  doChevalSearchEdit() {
    if (this.numeroEsrimaSearchEdit == '' && this.nomChevalSearchEdit == '') {
      this.messageService.add({severity: 'warn', summary: 'Attention', detail: 'Merci de saisir un N° ESRIMA ou Nom pour la recherche !'});
    } else {

      let params = {
        numeroEsrima: this.numeroEsrimaSearchEdit != '' ? this.numeroEsrimaSearchEdit : '',
        nomCheval: ''
      }

      this.http.search(environment.apiCheval + "search", params).subscribe(
        (res: any[]) => {
          this.chevals = res;
          if (this.chevals.length == 1) {
            this.cheval = this.chevals[0];
            this.nomChevalSearchEdit = this.chevals[0]["nom"];
            this.numeroEsrimaSearchEdit = this.chevals[0]["numeroEsrima"];
            this.chevalId = this.cheval["id"];
          } else if (this.chevals.length > 1) {
            this.selectionDisplay = true;
          } else {
            this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Aucun cheval trouvé !"});
          }
        }
      );
    }
  }

  saveReservation() {
    let dateTimeDebut = new Date(this.date);
      let dateTimeFin = new Date(this.date);
      dateTimeDebut.setHours(this.heureDebut);
      dateTimeDebut.setMinutes(this.minuteDebut);
      dateTimeFin.setHours(this.heureFin);
      dateTimeFin.setMinutes(this.minuteFin);
    if (this.action == "Ajouter") {
      let params = {
        dateDebut: dateTimeDebut,
        dateFin: dateTimeFin,
        idPersonneAFacturer: this.personneAFacturer["id"],
        idPersonne: "0",
        idBox: "0",
        idPiste: this.pisteId,
        idLit: "0",
        idCheval: this.cheval["id"],
        typeReservation: "CB",
        typePrix: this.typePrixDropdown["code"],
        idSession: this.sessionService.getItem("currentUser")["idSession"]
      };
      this.http.postWithParams(apiReservation + "creer", params).subscribe(res => {
        this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Réservation créée !'});
        setTimeout(() => {
          this.display = false;
          this.resetReservationInputs();
          var today = new Date();
          var dateDebut = new Date(today.getFullYear(), today.getMonth(), 1);
          var dateFin = new Date(today.getFullYear(), today.getMonth() + 1, 0);
          let args = new ChangeReservationArg("month", "init", 0, dateDebut, dateFin);
          this.onReservationChanged(args);
        }, 1500);
      }, error => {
        this.messageService.add({severity: 'error', summary: 'Erreur', detail: error.error.message});
      });
    }
    if (this.action == "Modifier") {
      let params = {
        idReservation: this.reservationId,
        dateDebut: dateTimeDebut,
        dateFin: dateTimeFin,
        idPersonneAFacturer: this.personneAFacturer["id"],
        idPersonne: "0",
        idBox: "0",
        idPiste: this.pisteId,
        idLit: "0",
        idCheval: this.chevalId,
        idSession: this.sessionService.getItem("currentUser")["idSession"]
      };
      this.http.postWithParams(apiReservation + "modifier", params).subscribe(res => {
        this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Réservation modifiée !'});
        setTimeout(() => {
          this.display = false;
          this.resetReservationInputs();
          var today = new Date();
          var dateDebut = new Date(today.getFullYear(), today.getMonth(), 1);
          var dateFin = new Date(today.getFullYear(), today.getMonth() + 1, 0);
          let args = new ChangeReservationArg("month", "init", 0, dateDebut, dateFin);
          this.onReservationChanged(args);
        }, 1500);
      }, error => {
        this.messageService.add({severity: 'error', summary: 'Erreur', detail: error.error.message});
      });
    }
  }

  closeReservationDialog() {
    this.display = false;
    this.resetReservationInputs();
  }

  resetReservationInputs() {
    this.nomSearch = "";
    this.prenomSearch = "";
    this.designationSearch = "";
    this.raisonSocialeSearch = "";
    this.cinSearch = "";
  }

  deleteReservation(reservation: any) {
    this.confirmationService.confirm({
      message: 'Etes-vous sûr de vouloir supprimer la réservation ?',
      accept: () => {
        this.http.delete(apiReservation, reservation["id"]).subscribe(res => {
          this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Réservation supprimée !'});
          setTimeout(() => {
            var today = new Date();
            var dateDebut = new Date(today.getFullYear(), today.getMonth(), 1);
            var dateFin = new Date(today.getFullYear(), today.getMonth() + 1, 0);
            let args = new ChangeReservationArg("month", "init", 0, dateDebut, dateFin);
            this.onReservationChanged(args);
          }, 1500);
        }, error => {
          this.messageService.add({severity: 'error', summary: 'Erreur', detail: error.error.message});
        });
      }
    });
  }

  closeSelectionDialog() {
    this.selectionDisplay = false;
  }

  selectionner(cheval: any) {
    if (this.action == "Ajouter") {
      this.cheval = cheval;
      this.nomChevalSearch = cheval.nom;
      this.numeroEsrimaSearch = cheval?.numeroEsrima;
      this.chevalId = cheval?.id;
      this.selectionDisplay = false;
    }
    if (this.action == "Modifier") {
      this.cheval = cheval;
      this.nomChevalSearchEdit = cheval.nom;
      this.numeroEsrimaSearchEdit = cheval?.numeroEsrima;
      this.chevalId = cheval?.id;
      this.selectionDisplay = false;
    }
  }

  personneSelectionner(personneAFacturer: any) {
    if (this.action == "Ajouter") {
      this.personneAFacturer = personneAFacturer;
      this.nomSearch = personneAFacturer["nom"];
      this.prenomSearch = personneAFacturer["prenom"];
      this.designationSearch = personneAFacturer["designation"];
      this.raisonSocialeSearch = personneAFacturer["raisonSociale"];
      this.cinSearch = personneAFacturer["numeroPieceIdentite"];
      this.personneAFacturerPersonneId = personneAFacturer["id"];
      this.personneSelectionDisplay = false;
    }
    if (this.action == "Modifier") {
      this.personneAFacturer = personneAFacturer;
      this.nomSearchEdit = personneAFacturer["nom"];
      this.prenomSearchEdit = personneAFacturer["prenom"];
      this.designationSearchEdit = personneAFacturer["designation"];
      this.raisonSocialeSearchEdit = personneAFacturer["raisonSociale"];
      this.cinSearchEdit = personneAFacturer["numeroPieceIdentite"];
      this.personneAFacturerPersonneId = personneAFacturer["id"];
      this.personneSelectionDisplay = false;
    }
  }

  onHeureDebutChange() {
    if (this.heureDebut == 12) {
      this.minuteDebut = 0;
      this.minuteFin = 30;
    }
    if (this.heureDebut > this.heureDebutOld) {
      if (this.heureFin - this.heureDebut >= 0) {
      } else {
        this.heureFin = this.heureDebut;
        this.heureDebutOld = this.heureDebut;
      }

    } else {
      this.heureDebutOld = this.heureDebut;
    }

  }

  onHeureFinChange() {
    if (this.heureDebut == 6 && this.minuteDebut == 0) {
      this.heureFin = 6;
      this.minuteFin = 30;
    }
    if (this.heureDebut > this.heureDebutOld) {
      if (this.heureFin - this.heureDebut >= 0) {
      } else {
        this.heureFin = this.heureDebut;
        this.heureDebutOld = this.heureDebut;
      }

    } else {
      this.heureDebutOld = this.heureDebut;
    }
  }

  onMinuteDebutChange() {
    if (this.heureDebut == 12) {
      this.minuteDebut = 0;
    }
    if (this.minuteDebut > this.minuteDebutOld) {
      this.minuteDebutOld = this.minuteDebut;
      if (this.minuteDebut == 30) {
        if (this.heureFin - this.heureDebut == 0) {
          this.heureFin = this.heureFin + 1;
        }
        this.minuteFin = 0;
      } else if (this.minuteDebut == 40) {
        if (this.heureFin - this.heureDebut == 0) {
          this.heureFin = this.heureFin + 1;
        }
        this.minuteFin = 10;
      } else if (this.minuteDebut == 50) {
        if (this.heureFin - this.heureDebut == 0) {
          this.heureFin = this.heureFin + 1;
        }
        this.minuteFin = 20;
      } else if (this.minuteDebut == 0) {
        this.heureDebut = this.heureDebut + 1;
      } else {
        this.minuteFin = this.minuteFin + 10;
      }
    } else {
      this.minuteDebutOld = this.minuteDebut;
    }


  }

  onMinuteFinChange() {
    if (this.heureDebut == 6 && this.minuteDebut == 0) {
      this.heureFin = 6;
      this.minuteFin = 30;
    }
  }

  editReservation(reservation) {
    for (let element of reservation.chevalPistes) {
      this.dateEdit = new Date(element.dateDebut);
      this.heureDebut = new Date(element.dateDebut).getUTCHours() + 1;
      this.heureFin = new Date(element.dateFin).getUTCHours() + 1;
      this.minuteDebut = new Date(element.dateDebut).getMinutes();
      this.minuteFin = new Date(element.dateFin).getMinutes();
      this.nomChevalSearchEdit = element.cheval.nom;
      this.numeroEsrimaSearchEdit = element.cheval.numeroEsrima;
      this.chevalId = element.cheval.id;
    }
    this.personneAFacturer = reservation.personneAFacturer.personne;
    if (reservation.personneAFacturer.personne.codeNaturePersonne == "P") {
      this.choixRaisonSociale = "P";
      this.typeRaisonSocialeSearch = this.raisonSocialeList[0];
      this.nomSearchEdit = reservation.personneAFacturer.personne.nom;
      this.prenomSearchEdit = reservation.personneAFacturer.personne.prenom;
      this.cinSearchEdit = reservation.personneAFacturer.personne.numeroPieceIdentite;
    }
    if (reservation.personneAFacturer.personne.codeNaturePersonne == "M") {
      this.choixRaisonSociale = "M";
      this.typeRaisonSocialeSearch = this.raisonSocialeList[2];
      this.raisonSocialeSearchEdit = reservation.personneAFacturer.personne.raisonSociale;
    }
    if (reservation.personneAFacturer.personne.codeNaturePersonne == "A") {
      this.choixRaisonSociale = "A";
      this.typeRaisonSocialeSearch = this.raisonSocialeList[1];
      this.designationSearchEdit = reservation.personneAFacturer.personne.designation;
    }
    this.reservationId = reservation["id"];
    this.action = "Modifier";
    this.editReservationDisplay = true;
    this.display = false;
  }

  closeEditReservationDialog() {
    this.editReservationDisplay = false;
    this.typeRaisonSocialeSearch = [];
    this.choixRaisonSociale = "";
    this.action = "Ajouter";
    this.display = true;
  }

}
