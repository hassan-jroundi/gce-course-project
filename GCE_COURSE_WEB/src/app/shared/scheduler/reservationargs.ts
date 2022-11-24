import { BookingDto } from "src/app/core/models/bookingdto";

export class ReservationArg {

  constructor(public roomid: number, public date: Date, public booking: BookingDto) { }

}
