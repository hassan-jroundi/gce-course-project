
export class BookingDto {
  bookingId: number;
  boxId: number;
  boxName: string;
  litName: string;
  roomId: number;
  roomType: number;
  startDate: Date;
  endDate: Date;
  stayDay: number;
  name: string;

  constructor() {
    this.bookingId = 0;
    this.boxName = '';
    this.litName = '';
    this.boxId = 0;
    this.roomId = 0;
    this.roomType = 0;
    this.startDate = undefined;
    this.endDate = undefined;
    this.stayDay = 0;
    this.name = '';
  }

}
