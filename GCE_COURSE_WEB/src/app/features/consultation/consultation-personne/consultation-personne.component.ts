import { SessionService } from './../../../core/services/session.service';
import { HttpService } from './../../../core/services/http.service';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { environment } from 'src/environments/environment';
import { RaisonSociale } from 'src/app/core/models/raison-sociale.model';
import { ChangeReservationArg } from 'src/app/shared/scheduler/changereservationarg';
import { Subscription } from 'rxjs';
import { BookingDto } from 'src/app/core/models/bookingdto';
import { Lit } from 'src/app/core/models/lit.model';
import { RoomDto } from 'src/app/core/models/roomdto';
import { ReservationArg } from 'src/app/shared/scheduler/reservationargs';
import {MessageService} from 'primeng/api';

let apiLit = environment.apiLit;
let apiReservation = environment.apiReservation;
let apiImmeuble = environment.apiImmeuble;
let apiPersonne = environment.apiPersonne;
let apiPersonneLit = environment.apiPersonneLit;
let apiChambre = environment.apiChambre;

@Component({
  selector: 'app-consultation-personne',
  templateUrl: './consultation-personne.component.html'
})
export class ConsultationPersonneComponent implements OnInit {

  nomSearch: any = '';
  prenomSearch: any = '';
  cinSearch: any = '';
  designationSearch: any = '';
  raisonSocialeSearch: any = '';
  typeRaisonSocialeSearch: any;

  personnes: any[];

  nom: any;
  prenom: any;
  cin: any;
  designation: any;
  raisonSociale: any;
  id: any;
  nomComplet: any;
  type: any;

  loading: boolean = false;
  display: boolean = false;
  selectionDisplay: boolean = false;

  raisonSocialeList: RaisonSociale[];
  raisonSocialeCatched: RaisonSociale;
  choixRaisonSociale: any;

  typeReservation: string;
  year: number;
  month: number;
  day: number;
  currentsearch: ChangeReservationArg;
  sub: Subscription;
  rooms: RoomDto[];
  bookings: BookingDto[];
  nombreReservations: number;

  constructor(private http: HttpService, private cd: ChangeDetectorRef, private sessionService: SessionService, private messageService: MessageService) {
    this.typeReservation="consultation lit";
    const d = new Date();
    this.year = d.getFullYear();
    this.month = d.getMonth() + 1;
    this.day = d.getDate();
    this.rooms = [];
    this.bookings = [];
    this.raisonSocialeList = [
      { name: 'Personne Physique', code: 'P' },
      { name: 'Personne Morale', code: 'M' },
      { name: 'Association', code: 'A' }
    ];
    this.typeRaisonSocialeSearch = this.raisonSocialeList[0];
    this.raisonSocialeCatched = this.raisonSocialeList[0];
    this.choixRaisonSociale = this.raisonSocialeCatched?.code;
  }

  ngOnInit() {
  }

  onRaisonSocialeChange(event) {
    this.raisonSocialeCatched = event.value;
    this.choixRaisonSociale = this.raisonSocialeCatched?.code;
    this.display = false;
    this.resetSearchInputs();
    this.resetNonSearchInputs();
  }

  resetSearchInputs() {
    this.nomSearch = '';
    this.prenomSearch = '';
    this.cinSearch = '';
    this.designationSearch = '';
    this.raisonSocialeSearch = '';
  }

  resetNonSearchInputs() {
    this.nom = '';
    this.prenom = '';
    this.cin = '';
    this.designation = '';
    this.raisonSociale = '';
  }

  doSearch() {
    this.display = false;
    if (this.nomSearch == '' && this.prenomSearch == '' && this.cinSearch == '' && this.designationSearch == '' && this.raisonSocialeSearch == '') {
      this.messageService.add({ severity: 'warn', summary: 'Attention', detail: 'Merci de saisir un élément avant de faire la recherche ' });
    } else {
      this.loading = true;
      let params = {
        nom: this.nomSearch,
        prenom: this.prenomSearch,
        cin: this.cinSearch,
        designation: this.designationSearch,
        raisonSociale: this.raisonSocialeSearch
      }

      this.http.search(apiPersonne + "search2", params).subscribe(
        (res: any[]) => {
          this.personnes = res;
          if (this.personnes.length == 1) {
            if (this.personnes[0]["nom"] != null) {
              this.type = "P";
            } else {
              this.type = "M";
            }
            this.loading = false;
            this.nom = this.personnes[0]["nom"];
            this.prenom = this.personnes[0]["prenom"];
            this.designation = this.personnes[0]["designation"];
            this.raisonSociale = this.personnes[0]["raisonSociale"];
            this.cin = this.personnes[0]["numeroPieceIdentite"];
            this.id = this.personnes[0]["id"];
            this.nomComplet = this.personnes[0]["nom"] + " " + this.personnes[0]["prenom"];
            this.display = true;
          } else if (this.personnes.length > 1) {
            this.loading = false;
            this.selectionDisplay = true;
          } else {
            this.loading = false;
            this.messageService.add({ severity: 'error', summary: 'Erreur', detail: "Aucune personne trouvée ! " });
          }
        }
      );
      setTimeout(() => {
        this.loading = false;
      }, 1500);
    }
  }

  closeSelectionDialog() {
    this.selectionDisplay = false;
  }

  selectionner(personne: any) {
    if (personne["nom"] != null) {
      this.type = "P";
    } else {
      this.type = "M";
    }
    this.nom = personne["nom"];
    this.prenom = personne["prenom"];
    this.designation = personne["designation"];
    this.raisonSociale = personne["raisonSociale"];
    this.cin = personne["numeroPieceIdentite"];
    this.id = personne["id"];
    this.nomComplet = personne["nom"] + " " + personne["prenom"];
    this.selectionDisplay = false;
    this.display = true;
  }

  onReservationChanged(args: ChangeReservationArg) {
    this.currentsearch = args;
    // this.roomsBox = [];
    if (this.sub) {
      this.sub.unsubscribe();
      this.sub = undefined;
    }
    this.sub = this.http.get(apiReservation + "personneLits/personne/" + this.id).subscribe((res: any[]) => {
      let resultat = res;
      this.nombreReservations = res.length;
      for (let element of res) {
        var dateDebut = new Date(element["dateDebut"]);
        var dateFin = new Date(element["dateFin"]);
        var dateDebutString = dateDebut.getDate() + "-" + (dateDebut.getMonth() + 1) + "-" + dateDebut.getFullYear();
        var dateFinString = dateFin.getDate() + "-" + (dateFin.getMonth() + 1) + "-" + dateFin.getFullYear();
        let booking = new BookingDto();
        booking.bookingId = element["id"];
        booking.roomId = element["personneLits"][0]["lit"]["id"];
        booking.boxName = element["personneLits"][0]["lit"]["nom"];
        booking.startDate = new Date(element["dateDebut"]);
        booking.endDate = new Date(element["dateFin"]);
        // booking.stayDay = element["chevalBoxs"][0]["dureeHebergement"];
        booking.name = "Réservation du : " + dateDebutString + " au " + dateFinString;
        this.bookings.push(booking);
        if (!(this.rooms.some(e => e.roomId == booking.roomId))) {
          let room = new RoomDto();
          room.roomId = element["personneLits"][0]["lit"]["id"];
          room.roomNumber = element["personneLits"][0]["lit"]["nom"];
          this.rooms.push(room);
        }
      }
    });
    this.cd.detectChanges();

  }

  reset() {
    this.display = false;
    this.nombreReservations = 0;
    this.bookings = [];
    this.rooms = [];
    this.resetSearchInputs();
    this.resetSearchInputs();
  }

}
