import { Chambre } from './../../../core/models/chambre.model';
import { ChangeFilterReservationArg } from './../../../shared/scheduler/changefilterreservationarg';
import { ConfirmationService } from 'primeng';
import { SessionService } from 'src/app/core/services/session.service';
import { element } from 'protractor';
import { HttpService } from './../../../core/services/http.service';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { Subscription } from 'rxjs';
import { BookingDto } from 'src/app/core/models/bookingdto';
import { ReservationDto } from 'src/app/core/models/reservationdto';
import { RoomDto } from 'src/app/core/models/roomdto';
import { ReservationService } from 'src/app/core/services/reservation-service';
import { ChangeReservationArg } from 'src/app/shared/scheduler/changereservationarg';
import { ReservationArg } from 'src/app/shared/scheduler/reservationargs';
import { FormReservationComponent } from './form-reservation/form-reservation.component';
import { environment } from 'src/environments/environment';
import { Box } from 'src/app/core/models/box.model';
import { RaisonSociale } from 'src/app/core/models/raison-sociale.model';
import {MessageService} from 'primeng/api';

let apiBox = environment.apiBox;
let apiReservation = environment.apiReservation;
let apiEcurie = environment.apiEcurie;
let apiPersonne = environment.apiPersonne;
let apiChevalBox = environment.apiChevalBox;
let apiChambre = environment.apiChambre;
let apiPersonneM = environment.apiPersonneM;

@Component({
  selector: 'app-reservation-box',
  templateUrl: './reservation-box.component.html'
})
export class ReservationBoxComponent implements OnInit {

  dateEnCours: Date = null;
  typePrixList: any[];
  room: RoomDto;
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

  raisonSocialeList: RaisonSociale[];
  nomSearch: any = '';
  prenomSearch: any = '';
  cinSearch: any = '';
  designationSearch: any = '';
  nomProprietaireSearch: any = '';
  raisonSocialeSearch: any = '';
  typeRaisonSocialeSearch: any;
  choixRaisonSociale: any;
  raisonSocialeCatched: RaisonSociale;
  action: any = '';
  ecurie: any;
  dateDebut: Date;
  dateFin: Date;

  nomChevalSearch: any = '';
  numeroEsrimaSearch: any = '';
  numeroTranspondeurSearch: any = '';

  chevals: any[];
  personneAFacturers: any[];
  ecurieDropdown: any;
  boxDropdown: any;
  typePrixDropdown: any;
  personneAFacturer: any;
  cheval: any;

  bookingEdit: any;
  personneAFacturerPersonneId: any;
  chevalId: any;
  enCours: boolean;
  invalidDates: Date[];

  chevalBoxs: any[];
  minDate: Date = new Date();
  minDateFin: Date;

  anterieureADateSystem: boolean = false;
  reservationFacturee: boolean = false;
  dateFinAnterieureADateSystem: boolean = false;

  profilUtilisateurConnecte: any;

  constructor(
    private dialog: MatDialog,
    private confirmationService: ConfirmationService,
    private service: ReservationService,
    private cd: ChangeDetectorRef,
    private http: HttpService,
    private sessionService: SessionService,
    private messageService: MessageService) {

    this.profilUtilisateurConnecte = this.sessionService.getItem("codeProfil");
    this.minDateFin = new Date(this.minDate.getTime() + (1000 * 60 * 60 * 24));
    this.typeReservation = "box";
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
    this.room = undefined;
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
    let args = null;
    if (this.dateEnCours != null) {
      let dateDebut = new Date(this.dateEnCours.getFullYear(), this.dateEnCours.getMonth(), 1);
      let dateFin = new Date(this.dateEnCours.getFullYear(), this.dateEnCours.getMonth()+1, 0);
      args = new ChangeReservationArg("month", "init", 0, dateDebut, dateFin);
    }  else {
      let today = new Date();
      let dateDebut = new Date(today.getFullYear(), today.getMonth(), 1);
      let dateFin = new Date(today.getFullYear(), today.getMonth()+1, 0);
      args = new ChangeReservationArg("month", "init", 0, dateDebut, dateFin);
    }
    this.loadEcuriesList();

    setTimeout(() => {
      this.onReservationChanged(args);
    }, 1500);
  }

  onRaisonSocialeChange(event) {
    this.raisonSocialeCatched = event.value;
    this.choixRaisonSociale = this.raisonSocialeCatched?.code;
    this.personneAFacturer = undefined;
    this.resetSearchInputs();
  }

  resetSearchInputs() {
    this.nomSearch = '';
    this.prenomSearch = '';
    this.cinSearch = '';
    this.designationSearch = '';
    this.raisonSocialeSearch = '';
  }

  onReservationChanged(args: ChangeReservationArg) {
    this.dateEnCours = args.startDate;
    this.currentsearch = args;
    // this.rooms = [];
    this.bookings = [];
    if (this.sub) {
      this.sub.unsubscribe();
      this.sub = undefined;
    }
    this.sub = this.http.get(apiBox + "site/" + this.sessionService.getItem("site")["nom"]).subscribe((res: Box[]) => {
      if (this.rooms.length == 0) {
        if (this.room != undefined) {
          this.rooms.push(this.room);
        } else {
          for (let element of res) {
            if (element.isActif == true) {
              let room = new RoomDto();
              room.roomId = element["id"];
              room.roomNumber = element["nom"];
              this.rooms.push(room);
            }
          }
        }
      }
      let params = {
        nomSite: this.sessionService.getItem("site")["nom"],
        mois: args.endDate.getMonth() + 1,
        annee: args.startDate.getFullYear()
      }
      this.http.search(apiReservation + "chevalBoxs/criteria", params).subscribe((res: any[]) => {
        let resultat = res;
        for (let element of res) {
          var dateDebut = new Date(element["dateDebut"]);
          var dateFin = new Date(element["dateFin"]);
          var dateDebutString = dateDebut.getDate() + "-" + (dateDebut.getMonth() + 1) + "-" + dateDebut.getFullYear();
          var dateFinString = dateFin.getDate() + "-" + (dateFin.getMonth() + 1) + "-" + dateFin.getFullYear();
          let booking = new BookingDto();
          booking.bookingId = element["id"];
          booking.roomId = element["chevalBoxs"][0]["box"]["id"];
          booking.boxName = element["chevalBoxs"][0]["box"]["nom"];
          booking.startDate = new Date(element["detailReservations"][0]["dateDebut"]);
          booking.endDate = new Date(element["detailReservations"][0]["dateFin"]);
          booking.stayDay = element["chevalBoxs"][0]["dureeHebergement"];
          booking.name = "Réservation du : " + dateDebutString + " au " + dateFinString + (element["chevalBoxs"][0]["cheval"] != null ? " - Cheval : " + element["chevalBoxs"][0]["cheval"]["nom"] : " - Sans Cheval");
          this.bookings.push(booking);
        }
      });
      this.cd.detectChanges();
    });

  }

  onEcurieReservationChanged(args: ChangeFilterReservationArg) {
    if (args.roomtype != 0) {
      this.rooms = [];
      if (this.sub) {
        this.sub.unsubscribe();
        this.sub = undefined;
      }
      this.sub = this.http.get(apiBox + "ecurie/" + args.roomtype).subscribe((res: Box[]) => {
        for (let element of res) {
          if (element.isActif == true) {
            let room = new RoomDto();
            room.roomId = element["id"];
            room.roomNumber = element["nom"];
            this.rooms.push(room);
          }
        }
        this.http.get(apiReservation + "chevalBoxs").subscribe((res: any[]) => {
          let resultat = res;
          for (let element of res) {
            var dateDebut = new Date(element["chevalBoxs"][0]["dateDebut"]);
            var dateFin = new Date(element["chevalBoxs"][0]["dateFin"]);
            var dateDebutString = dateDebut.getDate() + "-" + (dateDebut.getMonth() + 1) + "-" + dateDebut.getFullYear();
            var dateFinString = dateFin.getDate() + "-" + (dateFin.getMonth() + 1) + "-" + dateFin.getFullYear();
            let booking = new BookingDto();
            booking.bookingId = element["id"];
            booking.roomId = element["chevalBoxs"][0]["box"]["id"];
            booking.boxName = element["chevalBoxs"][0]["box"]["nom"];
            booking.startDate = new Date(element["chevalBoxs"][0]["dateDebut"]);
            booking.endDate = new Date(element["chevalBoxs"][0]["dateFin"]);
            booking.stayDay = element["chevalBoxs"][0]["dureeHebergement"];
            booking.name = "Réservation du : " + dateDebutString + " au " + dateFinString + " - Cheval : " + element["chevalBoxs"][0]["cheval"]["nom"];
            this.bookings.push(booking);
          }


        });
        this.cd.detectChanges();
      });
    } else {
      this.rooms = [];
      if (this.sub) {
        this.sub.unsubscribe();
        this.sub = undefined;
      }
      this.sub = this.http.get(apiBox).subscribe((res: Box[]) => {
        for (let element of res) {
          if (element.isActif == true) {
            let room = new RoomDto();
            room.roomId = element["id"];
            room.roomNumber = element["nom"];
            this.rooms.push(room);
          }
        }
        this.http.get(apiReservation + "chevalBoxs").subscribe((res: any[]) => {
          let resultat = res;
          for (let element of res) {
            var dateDebut = new Date(element["chevalBoxs"][0]["dateDebut"]);
            var dateFin = new Date(element["chevalBoxs"][0]["dateFin"]);
            var dateDebutString = dateDebut.getDate() + "-" + (dateDebut.getMonth() + 1) + "-" + dateDebut.getFullYear();
            var dateFinString = dateFin.getDate() + "-" + (dateFin.getMonth() + 1) + "-" + dateFin.getFullYear();
            let booking = new BookingDto();
            booking.bookingId = element["id"];
            booking.roomId = element["chevalBoxs"][0]["box"]["id"];
            booking.boxName = element["chevalBoxs"][0]["box"]["nom"];
            booking.startDate = new Date(element["chevalBoxs"][0]["dateDebut"]);
            booking.endDate = new Date(element["chevalBoxs"][0]["dateFin"]);
            booking.stayDay = element["chevalBoxs"][0]["dureeHebergement"];
            booking.name = "Réservation du : " + dateDebutString + " au " + dateFinString + " - Cheval : " + element["chevalBoxs"][0]["cheval"]["nom"];
            this.bookings.push(booking);
          }


        });
        this.cd.detectChanges();
      });
    }
  }

  onBoxReservationChanged(args: ChangeFilterReservationArg) {
    if (args.roomtype != 0) {
      this.rooms = [];
      if (this.sub) {
        this.sub.unsubscribe();
        this.sub = undefined;
      }
      this.sub = this.http.get(apiBox + args.roomtype).subscribe((res: Box) => {
        let room = new RoomDto();
        room.roomId = res["id"];
        room.roomNumber = res["nom"];
        this.room = room;
        this.rooms.push(room);
        this.http.get(apiReservation + "chevalBoxs").subscribe((res: any[]) => {
          let resultat = res;
          for (let element of res) {
            var dateDebut = new Date(element["chevalBoxs"][0]["dateDebut"]);
            var dateFin = new Date(element["chevalBoxs"][0]["dateFin"]);
            var dateDebutString = dateDebut.getDate() + "-" + (dateDebut.getMonth() + 1) + "-" + dateDebut.getFullYear();
            var dateFinString = dateFin.getDate() + "-" + (dateFin.getMonth() + 1) + "-" + dateFin.getFullYear();
            let booking = new BookingDto();
            booking.bookingId = element["id"];
            booking.roomId = element["chevalBoxs"][0]["box"]["id"];
            booking.boxName = element["chevalBoxs"][0]["box"]["nom"];
            booking.startDate = new Date(element["chevalBoxs"][0]["dateDebut"]);
            booking.endDate = new Date(element["chevalBoxs"][0]["dateFin"]);
            booking.stayDay = element["chevalBoxs"][0]["dureeHebergement"];
            booking.name = "Réservation du : " + dateDebutString + " au " + dateFinString + " - Cheval : " + element["chevalBoxs"][0]["cheval"]["nom"];
            this.bookings.push(booking);
          }


        });
        this.cd.detectChanges();
      });
    } else {
      this.room = undefined;
      this.rooms = [];
      if (this.sub) {
        this.sub.unsubscribe();
        this.sub = undefined;
      }
      this.sub = this.http.get(apiBox).subscribe((res: Box[]) => {
        for (let element of res) {
          if (element.isActif == true) {
            let room = new RoomDto();
            room.roomId = element["id"];
            room.roomNumber = element["nom"];
            this.rooms.push(room);
          }
        }
        this.http.get(apiReservation + "chevalBoxs").subscribe((res: any[]) => {
          let resultat = res;
          for (let element of res) {
            var dateDebut = new Date(element["chevalBoxs"][0]["dateDebut"]);
            var dateFin = new Date(element["chevalBoxs"][0]["dateFin"]);
            var dateDebutString = dateDebut.getDate() + "-" + (dateDebut.getMonth() + 1) + "-" + dateDebut.getFullYear();
            var dateFinString = dateFin.getDate() + "-" + (dateFin.getMonth() + 1) + "-" + dateFin.getFullYear();
            let booking = new BookingDto();
            booking.bookingId = element["id"];
            booking.roomId = element["chevalBoxs"][0]["box"]["id"];
            booking.boxName = element["chevalBoxs"][0]["box"]["nom"];
            booking.startDate = new Date(element["chevalBoxs"][0]["dateDebut"]);
            booking.endDate = new Date(element["chevalBoxs"][0]["dateFin"]);
            booking.stayDay = element["chevalBoxs"][0]["dureeHebergement"];
            booking.name = "Réservation du : " + dateDebutString + " au " + dateFinString + " - Cheval : " + element["chevalBoxs"][0]["cheval"]["nom"];
            this.bookings.push(booking);
          }


        });
        this.cd.detectChanges();
      });
    }
  }

  onDayReservation(args: ReservationArg) {
    this.http.get(apiChevalBox + "box/" + args["roomid"]).subscribe((res: any[]) => {
      this.chevalBoxs = res;
      for (let element of this.chevalBoxs) {
        if (!(new Date(args.booking.startDate).getTime() == new Date(element["dateDebut"]).getTime() && new Date(args.booking.endDate).getTime() == new Date(element["dateFin"]).getTime())) {
          let startTime = new Date(element["dateDebut"]).getTime();
          let endTime = new Date(element["dateFin"]).getTime();
          for (let loopTime = startTime; loopTime <= endTime; loopTime += 86400000) {
            var loopDay = new Date(loopTime);
            this.invalidDates.push(loopDay);
          }
        }
      }
    });

    this.http.get(apiBox + args["roomid"]).subscribe(res => {

      this.ecurieDropdown = res["ecurie"];
      this.http.get(apiBox + "ecurie/" + this.ecurieDropdown["id"]).subscribe(
        (resu: any[]) => {
          this.boxList = resu;
        }
      );
      this.boxDropdown = res;
    })
    if (args["booking"]["bookingId"] != 0) {
      this.action = "Modifier";
      this.bookingEdit = args;
      // this.dateDebut = args["booking"]["startDate"];
      // this.dateFin = args["booking"]["endDate"];
      this.http.get(apiChevalBox + "reservation/" + args["booking"]["bookingId"]).subscribe(res => {
        if (res["codeTypePrix"] === "U") {
          this.typePrixDropdown = this.typePrixList[0];
        } else {
          this.typePrixDropdown = this.typePrixList[1];
        }
        this.numeroEsrimaSearch = res["cheval"]["numeroEsrima"];
        this.nomChevalSearch = res["cheval"]["nom"];
        this.numeroTranspondeurSearch = res["cheval"]["transpondeur"];
        this.chevalId = res["cheval"]["id"];
        this.cheval = res["cheval"];
      });
      this.http.get(apiReservation + args["booking"]["bookingId"]).subscribe(res => {
        if (res["codeTypePrix"] == "F") {
          this.typePrixDropdown = this.typePrixList[0];
        } else {
          this.typePrixDropdown = this.typePrixList[1];
        }
        this.enCours = res["enCours"];
        this.dateDebut = new Date(res["dateDebut"]);
        this.dateFin = new Date(res["dateFin"]);
        if (this.dateDebut >= new Date()) {
          this.anterieureADateSystem = true;
        }
        if (this.dateFin > new Date()) {
          this.dateFinAnterieureADateSystem = true;
        }
        this.http.get(apiReservation + "reservationFacturee/" + args["booking"]["bookingId"]).subscribe((resuu: any) => {
          this.reservationFacturee = resuu;
          console.info("réservation déjà facturée ? ", resuu);
        });
        this.http.get(apiPersonneM + res["idPersonneFacture"]).subscribe(result => {
          this.personneAFacturer = result;
          this.personneAFacturerPersonneId = result["id"];
          if (result["codeNaturePersonne"] == "P") {
            this.nomSearch = result["nom"];
            this.prenomSearch = result["prenom"];
            this.cinSearch = result["numeroPieceIdentite"];
            this.typeRaisonSocialeSearch = this.raisonSocialeList[0];
            this.raisonSocialeCatched = this.typeRaisonSocialeSearch;
            this.choixRaisonSociale = this.raisonSocialeCatched?.code;
          }
          if (result["codeNaturePersonne"] == "A") {
            this.designationSearch = result["designation"];
            this.typeRaisonSocialeSearch = this.raisonSocialeList[2];
          }
          if (result["codeNaturePersonne"] == "M") {
            this.raisonSocialeSearch = result["raisonSociale"];
            this.typeRaisonSocialeSearch = this.raisonSocialeList[1];
          }
          this.choixRaisonSociale = this.typeRaisonSocialeSearch?.code;
        });
      });
    } else {
      this.action = "Ajouter";
      this.typePrixDropdown = this.typePrixList[0];
      let date = new Date(args["date"]);
      if (new Date(args["date"]).getTime() < new Date().getTime()) {
        this.dateDebut = new Date();
      } else {
        this.dateDebut = args["date"];
      }
      if (this.dateDebut.getMonth() >= 6) {
        this.dateFin = new Date(this.dateDebut.getFullYear() + 1, 11, 31)
      } else {
        this.dateFin = new Date(this.dateDebut.getFullYear(), 11, 31)
      }
      this.anterieureADateSystem = true;
      this.dateFinAnterieureADateSystem = true;
    }
    this.display = true;

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

  loadEcuriesList() {
    this.http.get(apiEcurie + "site/" + this.sessionService.getItem("site")["nom"]).subscribe(
      (res: any[]) => {
        this.ecuries = res;
      }
    )
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
            this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Aucune personne trouvée !"});
          }
        }
      );
    }
  }

  doChevalSearch() {
    if (this.nomChevalSearch == '' && this.numeroTranspondeurSearch == '' && this.nomProprietaireSearch == '') {
      this.messageService.add({severity: 'warn', summary: 'Attention', detail: 'Merci de saisir une valeur pour la recherche !'});
    } else {

      let params = {
        numeroEsrima: this.numeroEsrimaSearch != '' ? this.numeroEsrimaSearch : '',
        nomCheval: this.nomChevalSearch != '' ? this.nomChevalSearch : '',
        numeroTranspondeur: this.numeroTranspondeurSearch != '' ? this.numeroTranspondeurSearch : '',
        nomProprietaire: this.nomProprietaireSearch != '' ? this.nomProprietaireSearch : ''
      }

      this.http.search(environment.apiCheval + "search", params).subscribe(
        (res: any[]) => {
          this.chevals = res;
          if (this.chevals.length == 1) {
            if (this.chevals[0]["etat"] == "HENTR") {
              this.messageService.add({severity: 'warn', summary: 'Attention', detail: 'Le cheval ' + this.chevals[0]["nom"] + " est déclaré Hors Entrainement et ne peux être choisi."});
            } else {
              this.cheval = this.chevals[0];
              this.nomChevalSearch = this.chevals[0]["nom"];
              this.numeroEsrimaSearch = this.chevals[0]["numeroEsrima"];
              this.numeroTranspondeurSearch = this.chevals[0]["transpondeur"]
              this.chevalId = this.chevals[0]["id"];
              // this.http.get(apiPersonneM + "proprietaire/cheval/" + this.chevalId).subscribe(resu => {
              //   this.personneAFacturer = resu;
              //   this.personneAFacturerPersonneId = resu['id'];
              //   if (resu['codeNaturePersonne'] == 'P') {
              //     this.choixRaisonSociale = 'P';
              //     this.typeRaisonSocialeSearch = this.raisonSocialeList[0];
              //     this.nomSearch = resu['nom'];
              //     this.prenomSearch = resu['prenom'];
              //     this.cinSearch = resu['numeroPieceIdentite'];
              //   }
              //   if (resu['codeNaturePersonne'] == 'M') {
              //     this.choixRaisonSociale = 'M';
              //     this.typeRaisonSocialeSearch = this.raisonSocialeList[1];
              //     this.raisonSocialeSearch = resu['raisonSociale'];
              //   }
              //   if (resu['codeNaturePersonne'] == 'A') {
              //     this.choixRaisonSociale = 'A';
              //     this.typeRaisonSocialeSearch = this.raisonSocialeList[2];
              //     this.designationSearch = resu['designation'];
              //   }
              // });
            }
          } else if (this.chevals.length > 1) {
            this.selectionDisplay = true;
          } else {
            this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Aucun cheval trouvé !"});
          }
        }
      );
    }
  }

  selectionner(cheval: any) {
    this.cheval = cheval;
    this.nomChevalSearch = cheval.nom;
    this.numeroEsrimaSearch = cheval?.numeroEsrima;
    this.numeroTranspondeurSearch = cheval?.transpondeur;
    this.chevalId = cheval?.id;
    this.selectionDisplay = false;
    // this.http.get(apiPersonneM + "proprietaire/cheval/" + this.chevalId).subscribe(resu => {
    //   this.personneAFacturer = resu;
    //   this.personneAFacturerPersonneId = resu['id'];
    //   if (resu['codeNaturePersonne'] == 'P') {
    //     this.choixRaisonSociale = 'P';
    //     this.typeRaisonSocialeSearch = this.raisonSocialeList[0];
    //     this.nomSearch = resu['nom'];
    //     this.prenomSearch = resu['prenom'];
    //     this.cinSearch = resu['numeroPieceIdentite'];
    //   }
    //   if (resu['codeNaturePersonne'] == 'M') {
    //     this.choixRaisonSociale = 'M';
    //     this.typeRaisonSocialeSearch = this.raisonSocialeList[1];
    //     this.raisonSocialeSearch = resu['raisonSociale'];
    //   }
    //   if (resu['codeNaturePersonne'] == 'A') {
    //     this.choixRaisonSociale = 'A';
    //     this.typeRaisonSocialeSearch = this.raisonSocialeList[2];
    //     this.designationSearch = resu['designation'];
    //   }
    // });
  }

  closeSelectionDialog() {
    this.selectionDisplay = false;
  }

  personneSelectionner(personneAFacturer: any) {
    this.personneAFacturer = personneAFacturer;
    this.nomSearch = personneAFacturer["nom"];
    this.prenomSearch = personneAFacturer["prenom"];
    this.designationSearch = personneAFacturer["designation"];
    this.raisonSocialeSearch = personneAFacturer["raisonSociale"];
    this.cinSearch = personneAFacturer["numeroPieceIdentite"];
    this.personneAFacturerPersonneId = personneAFacturer["id"];
    this.personneSelectionDisplay = false;
  }

  saveReservation() {
    if (this.action == "Ajouter") {
      if (this.personneAFacturer == undefined || this.personneAFacturer == undefined) {
        this.messageService.add({severity: 'warn', summary: 'Attention', detail: 'Merci de remplir tous les champs nécessaires.'});
      } else {
        let params = {
          dateDebut: new Date(this.dateDebut),
          dateFin: new Date(this.dateFin),
          idPersonneAFacturer: this.personneAFacturer["id"],
          idPersonne: "0",
          idBox: this.boxDropdown["id"],
          idPiste: "0",
          idLit: "0",
          idCheval: this.cheval != undefined ? this.cheval["id"] : "",
          typePrix: this.typePrixDropdown["code"],
          typeReservation: "CB",
          idSession: this.sessionService.getItem("currentUser")["idSession"]
        };
        this.http.postWithParams(apiReservation + "creer", params).subscribe(res => {
          this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Réservation créée !'});
          setTimeout(() => {
            this.resetReservationInputs();
            this.display = false;
            this.ngOnInit();
          }, 1000);
        }, error => {
          this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Cette personne a déjà une réservation durant la période sélectionnée."});
        });
      }
    } else {
      let params = {
        idReservation: this.bookingEdit["booking"]["bookingId"],
        dateDebut: new Date(this.dateDebut),
        dateFin: new Date(this.dateFin),
        idPersonneAFacturer: this.personneAFacturerPersonneId,
        idPersonne: "0",
        idBox: this.boxDropdown["id"],
        idPiste: "0",
        idLit: "0",
        idCheval: (this.cheval != undefined && this.cheval != null) ? this.cheval["id"] : 0,
        typePrix: this.typePrixDropdown["code"],
        typeReservation: "CB",
        idSession: this.sessionService.getItem("currentUser")["idSession"]
      };
      console.info("paramètres envoyés modification box", params);
      console.info("this.cheval : ", this.cheval);
      this.http.postWithParams(apiReservation + "modifier", params).subscribe(res => {
        this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Réservation modifiée !'});
        setTimeout(() => {
          this.resetReservationInputs();
          this.display = false;
          this.ngOnInit();
        }, 1000);
      }, error => {
        this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Il n'est pas possible de modifier cette réservation."});
      });
    }
  }

  closeReservationDialog() {
    this.display = false;
    this.resetReservationInputs();
  }

  resetReservationInputs() {
    this.dateDebut = new Date();
    this.dateFin = new Date();
    this.nomSearch = "";
    this.prenomSearch = "";
    this.designationSearch = "";
    this.raisonSocialeSearch = "";
    this.cinSearch = "";
    this.enCours = null;
    this.numeroTranspondeurSearch = "";
    this.numeroEsrimaSearch = "";
    this.nomChevalSearch = "";
    this.choixRaisonSociale = "";
    this.typeRaisonSocialeSearch = [];
    this.cheval = undefined;
    this.personneAFacturer = undefined;
    this.anterieureADateSystem = false;
    this.dateFinAnterieureADateSystem = false;
  }

  onDateDebutChange(event) {
    let dateChoisie = new Date(event);
    this.dateFin = new Date(dateChoisie.getFullYear() + "-12-31");
    this.minDateFin = new Date(dateChoisie.getTime() + (1000 * 60 * 60 * 24));
  }

  onDateFinChange(event) {
  }

  addDays(date: Date, days: number): Date {
    date.setDate(date.getDate() + days);
    return date;
  }

  arreterReservation() {
    this.confirmationService.confirm({
      message: "Etes-vous sûr de vouloir arrêter la réservation ?",
      accept: () => {
        this.http.delete(apiReservation + "arreter/", this.bookingEdit["booking"]["bookingId"]).subscribe(res => {
          this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Réservation arrêtée !'});
          setTimeout(() => {
            this.resetReservationInputs();
            this.display = false;
            this.ngOnInit();
          }, 1000);
        }, error => {
          this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Il n'est pas possible d'arrêter cette réservation."});
        });
      }
    });
  }

  deleteReservation() {
    this.confirmationService.confirm({
      message: 'Etes-vous sûr de vouloir supprimer la réservation ?',
      accept: () => {
        this.http.delete(apiReservation, this.bookingEdit["booking"]["bookingId"]).subscribe(res => {
          this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Réservation supprimée !'});
          setTimeout(() => {
            this.resetReservationInputs();
            this.display = false;
            this.ngOnInit();
          }, 1000);
        }, error => {
          this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Il n'est pas possible de supprimer cette réservation."});
        });
      }
    });
  }

  resetChevalChoisi() {
    this.numeroTranspondeurSearch = "";
    this.numeroEsrimaSearch = "";
    this.nomChevalSearch = "";
    this.cheval = undefined;
  }

  resetPAFChoisi() {
    this.cinSearch = "";
    this.nomSearch = "";
    this.prenomSearch = "";
    this.raisonSocialeSearch = "";
    this.designationSearch = "";
    this.personneAFacturer = undefined;
    this.choixRaisonSociale = "";
    this.typeRaisonSocialeSearch = [];
  }
}
