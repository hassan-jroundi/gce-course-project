import { BookingDto } from "src/app/core/models/bookingdto";

export class StatusbarArg {

  constructor(public type: string, public booking: BookingDto) { }

}
