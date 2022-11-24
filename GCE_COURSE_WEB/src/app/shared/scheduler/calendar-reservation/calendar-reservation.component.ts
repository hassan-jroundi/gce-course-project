import { element } from 'protractor';
import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges } from '@angular/core';

import { DateAndWeek } from '../datemanager';
import { StatusbarArg } from '../changestatusbarargs';
import { ReservationArg } from '../reservationargs';
import { bindCallback } from 'rxjs';
import { RoomDto } from 'src/app/core/models/roomdto';
import { BookingDto } from 'src/app/core/models/bookingdto';

@Component({
  selector: 'app-reservation',
  templateUrl: './calendar-reservation.component.html',
  styleUrls: ['./calendar-reservation.component.css']
})
export class CalendarReservationComponent implements OnInit, OnChanges {

  @Input() finalLoading: boolean;
  @Input() room: RoomDto;
  @Input() day: DateAndWeek;
  @Input() bookings: BookingDto[];
  @Output() changestatusbar = new EventEmitter<StatusbarArg>();
  @Output() reservation = new EventEmitter<ReservationArg>();
  @Output() loading = new EventEmitter();
  spinnerLoading: boolean = false;
  isreserved = false;
  isreservedSx = false;
  isreservedCx = false;
  isreservedDx = false;
  isreserved0 = false;
  isreserved1 = false;
  isreserved6 = false;
  isreserved11 = false;
  isreserved16 = false;
  isreserved20 = false;
  reservedSize: number;
  booking: BookingDto;


  constructor() { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.bookings) {
      setTimeout(() => {
        this.datasourceChanged();
      }, 2100);
    }
  }

  onMouseEnter(b: BookingDto) {
    const args = new StatusbarArg('enter', b);
    this.changestatusbar.emit(args);
  }

  onMouseLeave(b: BookingDto) {
    const args = new StatusbarArg('leave', b);
    this.changestatusbar.emit(args);
  }

  onDayReservation(mouse: MouseEvent) {
    const b = new BookingDto();
    if (this.booking) {
      b.bookingId = this.booking.bookingId;
      b.roomId = this.booking.roomId;
      b.roomType = this.booking.roomType;
      b.startDate = new Date(this.booking.startDate);
      b.endDate = new Date(this.booking.endDate);
      b.stayDay = this.booking.stayDay;
      b.name = this.booking.name;
    }
    const args = new ReservationArg(this.room.roomId, this.day.date, b);
    this.reservation.emit(args);
  }

  private datasourceChanged() {
    this.isreserved = false;
    this.isreservedDx = false;
    this.isreservedCx = false;
    this.isreservedSx = false;
    this.booking = undefined;
    const list = this.bookings.filter(b => b.roomId === this.room.roomId);
    for (const b of list) {
      b.startDate.setHours(0,0,0,0);
      b.endDate.setHours(0,0,0,0);
      if (this.day.date >= b.startDate &&  this.day.date <= b.endDate) {
        this.isreserved = true;
        const d = this.day.date.getTime();
        if (d === b.startDate.getTime() && d !== b.endDate.getTime()) {
          this.booking = b;
          this.isreservedDx = true;
        }
        if (d !== b.startDate.getTime() && d !== b.endDate.getTime()) {
          this.booking = b;
          this.isreservedCx = true;
        }
        if (d !== b.startDate.getTime() && d === b.endDate.getTime()) {
          this.isreservedSx = true;
        }
      }
    }
  }

  // private async datasourceChanged() {
  //   this.isreserved = false;
  //   this.isreservedDx = false;
  //   this.isreservedCx = false;
  //   this.isreservedSx = false;
  //   this.isreserved0 = false;
  //   this.isreserved1 = false;
  //   this.isreserved6 = false;
  //   this.isreserved11 = false;
  //   this.isreserved16 = false;
  //   this.isreserved20 = false;
  //   this.booking = undefined;
  //
  //   if (this.room.roomTypeName == 'piste') {
  //     this.reservedSize = 0;
  //     for (let element of this.bookings) {
  //       let jourReservation: number = element["startDate"].getUTCDate();
  //       // console.info("month : ", element["startDate"].getMonth())
  //       // console.info("year : ", element["startDate"].getFullYear())
  //       // console.info("year utc : ", element["startDate"].getUTCFullYear())
  //       if (this.day.day == jourReservation && this.day.monthValue == element["startDate"].getMonth() + 1 && this.day.year == element["startDate"].getUTCFullYear()) {
  //         for (let i = element["startDate"].getHours(); i <= element["endDate"].getHours(); i++) {
  //           if (this.room.roomId == i) {
  //             this.reservedSize = this.reservedSize + 1;
  //             this.isreserved = true;
  //           }
  //         }
  //       }
  //     }
  //     this.spinnerLoading = true;
  //     this.loading.emit(this.spinnerLoading);
  //   } else {
  //     const list = this.bookings.filter(b => b.roomId === this.room.roomId);
  //     for (const b of list) {
  //       // if (this.day)
  //       if (this.day.date >= b.startDate && this.day.date <= b.endDate) {
  //         this.isreserved = true;
  //         const d = this.day.date.getTime();
  //         if (d === b.startDate.getTime() && d !== b.endDate.getTime()) {
  //           this.booking = b;
  //           this.isreservedDx = true;
  //         }
  //         if (d !== b.startDate.getTime() && d !== b.endDate.getTime()) {
  //           this.booking = b;
  //           this.isreservedCx = true;
  //         }
  //         if (d !== b.startDate.getTime() && d === b.endDate.getTime()) {
  //           this.isreservedSx = true;
  //         }
  //       }
  //     }
  //   }
  //
  // }

  addDays(date, days) {
    var result = new Date(date);
    result.setDate(result.getDate() + days);
    if (result.getHours() == 1) {
      result.setHours(result.getHours() - 1)
    }
    return result;
  }

  addHours(date, hours) {
    var result = new Date(date);
    result.setHours(result.getHours() + hours);
    return result;
  }

}
