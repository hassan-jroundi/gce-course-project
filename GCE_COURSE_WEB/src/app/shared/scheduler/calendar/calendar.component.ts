import { ChangeFilterReservationArg } from './../changefilterreservationarg';
import { SessionService } from 'src/app/core/services/session.service';
import { HttpService } from './../../../core/services/http.service';
import { Box } from './../../../core/models/box.model';
import { Component, Input, Output, EventEmitter, ViewChild, OnInit, SimpleChanges } from '@angular/core';

import { CalendarNavbarComponent } from '../calendar-navbar/calendar-navbar.component';
import { DateManager, DateAndWeek, StepHours } from '../datemanager';
import { ChangeReservationArg } from '../changereservationarg';
import { ChangeDateArg } from '../changedatearg';
import { HeaderDays } from '../model/headerdays';
import { StatusbarArg } from '../changestatusbarargs';
import { ReservationArg } from '../reservationargs';
import { RoomDto } from 'src/app/core/models/roomdto';
import { BookingDto } from 'src/app/core/models/bookingdto';
import { environment } from 'src/environments/environment';
import { info } from 'console';
let apiImmeuble = environment.apiImmeuble;

let apiEcurie = environment.apiEcurie;
let apiBox = environment.apiBox;
let apiChambre = environment.apiChambre;
let apiPiste = environment.apiPiste;

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.css']
})
export class CalendarComponent implements OnInit {

  @Input() nomCheval: string;
  @Input() typePersonne: string;
  @Input() nomPersonne: string;
  @Input() typeReservation: string;
  @Input() nombreReservations: number;
  @Input() year: number;
  @Input() month: number;
  @Input() day: number;
  @Input() rooms: RoomDto[];
  @Input() boxs: Box[];
  @Input() bookings: BookingDto[];
  @Output() changereservation = new EventEmitter<ChangeReservationArg>();
  @Output() reservation = new EventEmitter<ReservationArg>();
  @Output() filterecuriereservation = new EventEmitter<ChangeFilterReservationArg>();
  @Output() filtreboxreservation = new EventEmitter<ChangeFilterReservationArg>();
  @Output() filterimmeublereservation = new EventEmitter<ChangeFilterReservationArg>();
  @Output() filtrelitreservation = new EventEmitter<ChangeFilterReservationArg>();
  @Output() filtrechambrereservation = new EventEmitter<ChangeFilterReservationArg>();
  @Output() filtrepistereservation = new EventEmitter<ChangeFilterReservationArg>();
  @ViewChild(CalendarNavbarComponent) navbar;
  stepdays: DateAndWeek[] = [];
  stephours: StepHours[] = [];
  headerdays: HeaderDays;
  statusbar: BookingDto;
  manager: DateManager;

  roomsTypeList: any[];
  ecurieList: any[];
  immeubleList: any[];
  chambreList: any;
  boxList: any[];
  pisteList: any[];

  ecurieChoosen: any;
  boxChoosen: any;
  immeubleChoosen: any;
  chambreChoosen: any;
  pisteChoosen: any;

  constructor(private http: HttpService, private sessionService: SessionService) {
    this.loadEcurieList();
    this.loadImmeublesList();
    this.loadPisteList();
    this.manager = new DateManager();
    this.rooms = [];
    this.boxs = [];
    this.bookings = [];
  }

  get currentYMD(): Date {
    if (this.navbar) {
      return this.navbar.currymd;
    } else {
      return undefined;
    }
  }

  ngOnInit() {
  }

  loadEcurieList() {
    this.http.get(apiEcurie + "site/" + this.sessionService.getItem("site")["nom"]).subscribe(
      (res: any[]) => {
        this.ecurieList = res;
        this.ecurieChoosen = this.ecurieList[0];
        this.http.get(apiBox + "ecurie/" + this.ecurieChoosen["id"]).subscribe((res: Box[]) => {
          this.boxList = res.filter(e => e.isActif == true);
        });
        const args = new ChangeFilterReservationArg(this.ecurieChoosen["id"]);
        this.filterecuriereservation.emit(args);
      }
    )
  }

  loadImmeublesList() {
    this.http.get(apiImmeuble + "site/" + this.sessionService.getItem("site")["nom"]).subscribe(
      (res: any[]) => {
        this.immeubleList = res.filter(e => e.isActif == true);
        this.immeubleChoosen = this.immeubleList[0];
        this.http.get(apiChambre + "immeuble/" + this.immeubleChoosen["id"]).subscribe((res: any[]) => {
          this.chambreList = res.filter(e => e.isActif == true);
          this.chambreChoosen = this.chambreList[0];
          const args = new ChangeFilterReservationArg(this.chambreChoosen["id"]);
          this.filtrechambrereservation.emit(args);
        });
      }
    )
  }

  loadPisteList() {
    this.http.get(apiPiste + "site/" + this.sessionService.getItem("site")["nom"]).subscribe(
      (res: any[]) => {
        this.pisteList = res;
        this.pisteChoosen = res[0];
        const args = new ChangeFilterReservationArg(this.pisteChoosen != null ? this.pisteChoosen.id : 0);
        this.filtrepistereservation.emit(args);
      }
    );

  }

  onEcurieChange(data) {
    if (data.value != null) {
      this.ecurieChoosen = data.value;
      this.http.get(apiBox + "ecurie/" + this.ecurieChoosen["id"]).subscribe((res: Box[]) => {
        this.boxList = res.filter(e => e.isActif == true);
      });
    } else {
      this.boxList = [];
    }
    const args = new ChangeFilterReservationArg(data.value != null ? data.value.id : 0);
    this.filterecuriereservation.emit(args);
  }

  onPisteChange(data) {
    if (data.value != null) {
      this.pisteChoosen = data.value;
    } else {

    }
    const args = new ChangeFilterReservationArg(data.value != null ? data.value.id : 0);
    this.filtrepistereservation.emit(args);
  }

  onImmeubleChange(data) {
    if (data.value != null) {
      this.immeubleChoosen = data.value;
      this.http.get(apiChambre + "immeuble/" + this.immeubleChoosen["id"]).subscribe((res: any[]) => {
        this.chambreList = res.filter(e => e.isActif == true);
      });
    } else {
      this.chambreList = [];
    }
    const args = new ChangeFilterReservationArg(data.value != null ? data.value.id : 0);
    this.filterimmeublereservation.emit(args);
  }

  onChambreChange(data) {
    if (data.value != null) {
      this.chambreChoosen = data.value;
    } else {
    }
    const args = new ChangeFilterReservationArg(data.value != null ? data.value.id : 0);
    this.filtrechambrereservation.emit(args);
  }

  onBoxChange(data) {
    if (data.value != null) {
      this.boxChoosen = data.value;
    } else {
    }
    const args = new ChangeFilterReservationArg(data.value != null ? data.value.id : 0);
    this.filtreboxreservation.emit(args);
  }

  onDaysChanged(data: ChangeDateArg) {
    this.headerdays = data.days;
    const startDate = data.days.startDate;
    const endDate = data.days.endDate;
    const roomtype = data.roomtype;
    const args = new ChangeReservationArg(data.type, data.operation, roomtype, startDate, endDate);
    this.changereservation.emit(args);
  }

  onStatusbarChanged(args: StatusbarArg) {
    if (args.type === 'enter') {
      this.statusbar = args.booking;
    } else {
      this.statusbar = undefined;
    }
  }

  onDayReservation(args: ReservationArg) {
    this.reservation.emit(args);
  }

}
