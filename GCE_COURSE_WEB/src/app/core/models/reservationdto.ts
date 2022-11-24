import { Box } from './box.model';
import { BookingDto } from './bookingdto';
import { RoomDto } from './roomdto';
import { Lit } from './lit.model';

export class ReservationDto {
  lits: Lit[];
  boxs: Box[];
  rooms: RoomDto[];
  bookings: BookingDto[];
}
