import { HttpService } from './../../../core/services/http.service';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Subscription } from 'rxjs';
import { BookingDto } from 'src/app/core/models/bookingdto';
import { RoomDto } from 'src/app/core/models/roomdto';
import { ChangeReservationArg } from 'src/app/shared/scheduler/changereservationarg';
import { SessionService } from 'src/app/core/services/session.service';
import { Box } from 'src/app/core/models/box.model';
import { ReservationService } from 'src/app/core/services/reservation-service';
import {MessageService} from 'primeng/api';

let apiCheval = environment.apiCheval;
let apiBox = environment.apiBox;
let apiPiste = environment.apiPiste;
let apiReservation = environment.apiReservation;

@Component({
  selector: 'app-consultation-cheval',
  templateUrl: './consultation-cheval.component.html'
})
export class ConsultationChevalComponent implements OnInit {

  numeroEsrimaSearch: any = '';
  numeroTranspondeurSearch: any = '';
  nomChevalSearch: any = '';
  loading: boolean = false;
  display: boolean = false;
  selectionDisplay: boolean = false;

  chevals: any[];

  nomCheval: any;
  numeroEsrima: any;
  numeroTranspondeur: any;
  libelleEtat: any;
  etat: any;
  id: any;

  typeReservation: string;
  typeReservationBox: string;
  typeReservationPiste: string;
  year: number;
  month: number;
  day: number;
  currentsearch: ChangeReservationArg;
  sub: Subscription;
  subBox: Subscription;
  subPiste: Subscription;
  rooms: RoomDto[];
  roomsBox: RoomDto[] = [];
  roomsPiste: RoomDto[] = [];
  bookings: BookingDto[];
  bookingsBox: BookingDto[];
  bookingsPiste: BookingDto[];
  nombreReservations: number;
  nombreReservationsBox: number;
  nombreReservationsPiste: number;

  constructor(private http: HttpService, private cd: ChangeDetectorRef, private sessionService: SessionService, private service: ReservationService, private messageService: MessageService) {
    this.typeReservation = "consultation box";
    this.typeReservationBox = "consultation box";
    this.typeReservationPiste = "consultation piste";
    const d = new Date();
    this.year = d.getFullYear();
    this.month = d.getMonth() + 1;
    this.day = d.getDate();
    this.rooms = [];
    this.bookings = [];
    this.bookingsBox = [];
    this.bookingsPiste = [];
  }

  ngOnInit() { }

  doSearch() {
    this.display = false;
    if (this.numeroEsrimaSearch == '' && this.numeroTranspondeurSearch == '' && this.nomChevalSearch == '') {
      this.messageService.add({ severity: 'warn', summary: 'Attention', detail: 'Merci de saisir un N° ESRIMA ou N° Transpondeur pour la recherche ! ' });
    } else {
      this.loading = true;
      let params = {
        numeroEsrima: this.numeroEsrimaSearch != '' ? this.numeroEsrimaSearch : '',
        numeroTranspondeur: this.numeroTranspondeurSearch != '' ? this.numeroTranspondeurSearch : '',
        nomCheval: this.nomChevalSearch != '' ? this.nomChevalSearch : '',
        nomProprietaire: ''
      }

      this.http.search(environment.apiCheval + "search", params).subscribe(
        (res: any[]) => {
          this.chevals = res;
          if (this.chevals.length == 1) {
            this.nomCheval = this.chevals[0]["nom"];
            this.numeroEsrima = this.chevals[0]["numeroEsrima"];
            this.numeroTranspondeur = this.chevals[0]["transpondeur"];
            this.id = this.chevals[0]["id"];
            this.libelleEtat = this.chevals[0]["libelleEtat"];
            this.etat = this.chevals[0]["statut"];
            this.loading = false;
            this.display = true;
          } else if (this.chevals.length > 1) {
            this.loading = false;
            this.selectionDisplay = true;
          } else {
            this.loading = false;
            this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Aucun cheval trouvé" });
          }
        }
      );
      setTimeout(() => {
        this.loading = false;
      }, 1500);
    }

    setTimeout(() => {
      this.loading = false;
    }, 1300);
  }

  closeSelectionDialog() {
    this.selectionDisplay = false;
  }

  selectionner(cheval: any) {
    this.nomCheval = cheval.nom;
    this.numeroEsrima = cheval?.numeroEsrima;
    this.numeroTranspondeur = cheval?.numeroTranspondeur;
    this.id = cheval?.id;
    this.etat = cheval?.statut;
    this.libelleEtat = cheval?.libelleEtat;
    this.selectionDisplay = false;
    this.display = true;
  }

  onBoxReservationChanged(args: ChangeReservationArg) {
    this.currentsearch = args;
    // this.roomsBox = [];
    if (this.subBox) {
      this.subBox.unsubscribe();
      this.subBox = undefined;
    }
    this.subBox = this.http.get(apiReservation + "chevalBoxs/cheval/" + this.id).subscribe((res: any[]) => {
      let resultat = res;
      this.nombreReservationsBox = res.length;
      for (let element of res) {
        var dateDebut = new Date(element["dateDebut"]);
        var dateFin = new Date(element["dateFin"]);
        var dateDebutString = dateDebut.getDate() + "-" + (dateDebut.getMonth() + 1) + "-" + dateDebut.getFullYear();
        var dateFinString = dateFin.getDate() + "-" + (dateFin.getMonth() + 1) + "-" + dateFin.getFullYear();
        let booking = new BookingDto();
        booking.bookingId = element["id"];
        booking.roomId = element["chevalBoxs"][0]["box"]["id"];
        booking.boxName = element["chevalBoxs"][0]["box"]["nom"];
        booking.startDate = new Date(element["dateDebut"]);
        booking.endDate = new Date(element["dateFin"]);
        // booking.stayDay = element["chevalBoxs"][0]["dureeHebergement"];
        booking.name = "Réservation du : " + dateDebutString + " au " + dateFinString;
        this.bookingsBox.push(booking);
        if (this.roomsBox.length == 0) {
            if (!(this.rooms.some(e => e.roomId == booking.roomId))) {
                let room = new RoomDto();
                room.roomId = element["chevalBoxs"][0]["box"]["id"];
                room.roomNumber = element["chevalBoxs"][0]["box"]["nom"];
                this.roomsBox.push(room);
            }
        }
      }
    });
    this.cd.detectChanges();
  }

  onPisteReservationChanged(args: ChangeReservationArg) {

    this.bookingsPiste = [];
    this.currentsearch = args;
    this.roomsPiste = this.service.getPisteHours();
    if (this.subPiste) {
      this.subPiste.unsubscribe();
      this.subPiste = undefined;
    }
    this.subPiste = this.http.get(apiReservation + "chevalPistes/cheval/" + this.id).subscribe(
      (res: any[]) => {
        this.nombreReservationsPiste = res.length;
        for (let element of res) {
          var date = new Date(element["chevalPistes"][0]["dateDebut"]);
          var dateString = date.getDate() + "-" + (date.getMonth() + 1) + "-" + date.getFullYear();
          var dateDebut = new Date(element["chevalPistes"][0]["dateDebut"]);
          var dateFin = new Date(element["chevalPistes"][0]["dateFin"]);
          var dateDebutString = dateDebut.getHours() + "h" + dateDebut.getMinutes();
          var dateFinString = dateFin.getHours() + "h" + dateFin.getMinutes();
          let booking = new BookingDto();
          booking.bookingId = element["id"];
          booking.startDate = new Date(element["chevalPistes"][0]["dateDebut"]);
          booking.endDate = new Date(element["chevalPistes"][0]["dateFin"]);
          booking.stayDay = element["chevalPistes"][0]["dureeHebergement"];
          booking.name = "Réservation : " + dateString + " de " + dateDebutString + " à " + dateFinString;
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
          this.bookingsPiste.push(booking);
        }

      }
    );
    this.cd.detectChanges();
  }

  reset() {
    this.display = false;
    this.nomCheval = "";
    this.numeroEsrima = "";
    this.numeroTranspondeur = "";
    this.numeroEsrimaSearch = "";
    this.numeroTranspondeurSearch = "";
    this.nombreReservationsBox = 0;
    this.nombreReservationsPiste = 0;
    this.roomsBox = [];
    this.bookingsBox = [];
    this.bookingsPiste = [];
  }

}
