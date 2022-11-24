import {Immeuble} from './../../../core/models/immeuble.model';
import {element} from 'protractor';
import {Chambre} from './../../../core/models/chambre.model';
import {Lit} from './../../../core/models/lit.model';
import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {RoomDto} from 'src/app/core/models/roomdto';
import {ChangeReservationArg} from 'src/app/shared/scheduler/changereservationarg';
import {environment} from 'src/environments/environment';
import {BookingDto} from 'src/app/core/models/bookingdto';
import {RaisonSociale} from 'src/app/core/models/raison-sociale.model';
import {MatDialog} from '@angular/material';
import {ConfirmationService} from 'primeng';
import {ReservationService} from 'src/app/core/services/reservation-service';
import {HttpService} from 'src/app/core/services/http.service';
import {SessionService} from 'src/app/core/services/session.service';
import {ChangeFilterReservationArg} from 'src/app/shared/scheduler/changefilterreservationarg';
import {ReservationArg} from 'src/app/shared/scheduler/reservationargs';
import {Box} from 'src/app/core/models/box.model';
import {MessageService} from 'primeng/api';

let apiLit = environment.apiLit;
let apiReservation = environment.apiReservation;
let apiImmeuble = environment.apiImmeuble;
let apiPersonne = environment.apiPersonne;
let apiPersonneLit = environment.apiPersonneLit;
let apiChambre = environment.apiChambre;
let apiPersonneM = environment.apiPersonneM;

@Component({
  selector: 'app-reservation-lit',
  templateUrl: './reservation-lit.component.html'
})
export class ReservationLitComponent implements OnInit {

  dateEnCours: Date = null;
  typePrixList: any[];
  nomEmployeur: any = '';
  immeubleId: any;
  room: RoomDto;
  chambre: any;
  immeuble: any;
  immeubles: any[];
  chambres: any[];
  typeReservation: string;
  year: number;
  month: number;
  day: number;
  currentsearch: ChangeReservationArg;
  sub: Subscription;
  rooms: RoomDto[];
  lits: Lit[];
  litList: any[] = [];
  bookings: BookingDto[];
  display: boolean = false;
  selectionDisplay: boolean = false;
  personneSelectionDisplay: boolean = false;

  raisonSocialeList: RaisonSociale[];
  nomSearch: any = '';
  prenomSearch: any = '';
  cinSearch: any = '';
  designationSearch: any = '';
  raisonSocialeSearch: any = '';
  typeRaisonSocialeSearch: any;
  choixRaisonSociale: any;
  raisonSocialeCatched: RaisonSociale;
  action: any = '';
  ecurie: any;
  dateDebut: Date = new Date();
  dateFin: Date;

  nomPersonneSearch: any = '';
  prenomPersonneSearch: any = '';
  cinPersonneSearch: any = '';
  agePersonneSearch: any = '';

  personnes: any[];
  personneAFacturers: any[];
  immeubleDropdown: any;
  chambreDropdown: any;
  litDropdown: any;
  typePrixDrowdown: any;
  personneAFacturer: any;
  personne: any;
  enCours: boolean;

  bookingEdit: any;
  personneAFacturerPersonneId: any;
  personneId: any;
  invalidDates: Date[];

  personneLits: any[];

  minDate: Date = new Date();
  minDateFin: Date;

  profilUtilisateurConnecte: any;

  anterieureADateSystem: boolean = false;
  dateFinAnterieureADateSystem: boolean = false;

  constructor(private dialog: MatDialog,
              private confirmationService: ConfirmationService,
              private service: ReservationService,
              private cd: ChangeDetectorRef,
              private http: HttpService,
              private sessionService: SessionService,
              private messageService: MessageService) {

    this.profilUtilisateurConnecte = this.sessionService.getItem('codeProfil');
    this.minDateFin = new Date(this.minDate.getTime() + (1000 * 60 * 60 * 24));
    this.typeReservation = 'lit';
    const d = new Date();
    this.year = d.getFullYear();
    this.month = d.getMonth() + 1;
    this.day = d.getDate();
    this.rooms = [];
    this.room = undefined;
    this.bookings = [];
    this.lits = [];
    this.invalidDates = [];
    this.dateDebut = new Date();
    this.dateFin = new Date();
    this.raisonSocialeList = [
      {name: 'Personne Physique', code: 'P'},
      {name: 'Personne Morale', code: 'M'},
      {name: 'Association', code: 'A'}
    ];
    this.typePrixList = [
      {name: 'Forfaitaire', code: 'F'},
      {name: 'Unitaire', code: 'U'}
    ];
  }

  ngOnInit() {
    let args = null;
    if (this.dateEnCours != null) {
      let dateDebut = new Date(this.dateEnCours.getFullYear(), this.dateEnCours.getMonth(), 1);
      let dateFin = new Date(this.dateEnCours.getFullYear(), this.dateEnCours.getMonth()+1, 0);
      args = new ChangeReservationArg("month", "init", 0, dateDebut, dateFin);
    } else {
      let today = new Date();
      let dateDebut = new Date(today.getFullYear(), today.getMonth(), 1);
      let dateFin = new Date(today.getFullYear(), today.getMonth()+1, 0);
      args = new ChangeReservationArg("month", "init", 0, dateDebut, dateFin);
    }
    this.loadImmeublesList();
    setTimeout(() => {
      this.onReservationChanged(args);
    }, 1500);
  }

  loadImmeublesList() {
    this.http.get(apiImmeuble + 'site/' + this.sessionService.getItem('site')['nom']).subscribe(
      (res: any[]) => {
        this.immeubles = res;
      }
    );
  }

  onReservationChanged(args: ChangeReservationArg) {
    this.dateEnCours = args.startDate;
    this.currentsearch = args;
    this.bookings = [];
    if (this.sub) {
      this.sub.unsubscribe();
      this.sub = undefined;
    }
    this.sub = this.http.get(apiLit + 'site/' + this.sessionService.getItem('site')['nom']).subscribe((res: Lit[]) => {
      if (this.rooms.length == 0) {
        if (this.room != undefined) {
          this.http.get(apiLit + 'chambre/' + this.room.roomId).subscribe(
            (res: Lit[]) => {
              for (let element of res) {
                if (element.isActif == true && element.chambre.isActif == true && element.chambre.immeuble.isActif == true) {
                  let room = new RoomDto();
                  room.roomId = element['id'];
                  room.roomNumber = element['nom'];
                  this.rooms.push(room);
                }
              }
            }
          );
        } else {
          for (let element of res) {
            if (element.isActif == true && element.chambre.isActif == true && element.chambre.immeuble.isActif == true) {
              let room = new RoomDto();
              room.roomId = element['id'];
              room.roomNumber = element['nom'];
              this.rooms.push(room);
            }
          }
        }
      }
      let params = {
        nomSite: this.sessionService.getItem('site')['nom'],
        mois: args.endDate.getMonth() + 1,
        annee: args.startDate.getFullYear()
      };
      this.http.search(apiReservation + 'personneLits/criteria', params).subscribe((res: any[]) => {
        let resultat = res;
        for (let element of res) {
          var dateDebut = new Date(element['dateDebut']);
          var dateFin = new Date(element['dateFin']);
          var dateDebutString = dateDebut.getDate() + '-' + (dateDebut.getMonth() + 1) + '-' + dateDebut.getFullYear();
          var dateFinString = dateFin.getDate() + '-' + (dateFin.getMonth() + 1) + '-' + dateFin.getFullYear();
          let booking = new BookingDto();
          booking.bookingId = element['id'];
          booking.roomId = element['personneLits'][0]['lit']['id'];
          booking.boxName = element['personneLits'][0]['lit']['nom'];
          booking.litName = element['personneLits'][0]['lit']['nom'];
          booking.startDate = new Date(element['detailReservations'][0]['dateDebut']);
          booking.endDate = new Date(element['detailReservations'][0]['dateFin']);
          // booking.stayDay = element["personneLits"][0]["dureeHebergement"];
          booking.name = 'Réservation du : ' + dateDebutString + ' au ' + dateFinString + ' - Personne : ' + element['personneLits'][0]['personne']['nom'] + ' ' + element['personneLits'][0]['personne']['prenom'];
          this.bookings.push(booking);
        }

      });
      this.cd.detectChanges();


    });

  }

  onDayReservation(args: ReservationArg) {
    this.http.get(apiPersonneLit + 'lit/' + args['roomid']).subscribe((res: any[]) => {
      this.personneLits = res;
      for (let element of this.personneLits) {

        let startTime = new Date(element['dateDebut']).getTime();
        let endTime = new Date(element['dateFin']).getTime();
        for (let loopTime = startTime; loopTime <= endTime; loopTime += 86400000) {
          var loopDay = new Date(loopTime);
          this.invalidDates.push(loopDay);
        }
      }
    });

    this.http.get(apiLit + args['roomid']).subscribe(res => {

      this.loadImmeublesList();
      this.immeubleDropdown = res['chambre']['immeuble'];
      this.http.get(apiChambre + 'immeuble/' + res['chambre']['immeuble']['id']).subscribe(
        (result: any[]) => {
          this.chambres = result;
          this.chambreDropdown = res['chambre'];
          this.http.get(apiLit + 'chambre/' + this.chambreDropdown['id']).subscribe(
            (resultat: any[]) => {
              this.lits = resultat;
              this.litDropdown = res;
            }
          );
        });
    });
    if (args['booking']['bookingId'] != 0) {
      this.action = 'Modifier';
      this.bookingEdit = args;
      // this.dateDebut = args["booking"]["startDate"];
      // this.dateFin = args["booking"]["endDate"];
      this.http.get(apiPersonneLit + 'reservation/' + args['booking']['bookingId']).subscribe(res => {

        // remplir les champs lors de la modification d'une réservation - à faire ici !!!!!
        this.personne = res['personne'];
        this.personneId = res['personne']['id'];
        this.nomPersonneSearch = res['personne']['nom'];
        this.prenomPersonneSearch = res['personne']['prenom'];
        this.cinPersonneSearch = res['personne']['numeroPieceIdentite'];
        this.http.get(apiPersonne + 'employeur/' + this.personneId).subscribe(resu => {
          if (resu['codeNaturePersonne'] == 'P') {
            this.nomEmployeur = resu['nom'] + ' ' + resu['prenom'];
          }
          if (resu['codeNaturePersonne'] == 'M') {
            this.nomEmployeur = resu['raisonSociale'];
            this.choixRaisonSociale = 'M';
          }
          if (resu['codeNaturePersonne'] == 'A') {
            this.nomEmployeur = resu['designation'];
            this.choixRaisonSociale = 'A';
          }
        });
      });
      this.http.get(apiReservation + args['booking']['bookingId']).subscribe(res => {
        this.enCours = res["enCours"];
        this.dateDebut = new Date(res['dateDebut']);
        this.dateFin = new Date(res['dateFin']);
        if (this.dateDebut >= new Date()) {
          this.anterieureADateSystem = true;
        }
        if (this.dateFin >= new Date()) {
          this.dateFinAnterieureADateSystem = true;
        }
        if (res['codeTypePrix'] === 'F') {
          this.typePrixDrowdown = this.typePrixList[0];
        } else {
          this.typePrixDrowdown = this.typePrixList[1];
        }
        this.http.get(apiPersonneM + res['idPersonneFacture']).subscribe(result => {
          this.personneAFacturer = result;
          this.personneAFacturerPersonneId = result['id'];
          if (result['codeNaturePersonne'] == 'P') {
            this.nomSearch = result['nom'];
            this.prenomSearch = result['prenom'];
            this.cinSearch = result['numeroPieceIdentite'];
            this.typeRaisonSocialeSearch = this.raisonSocialeList[0];
            this.raisonSocialeCatched = this.typeRaisonSocialeSearch;
          }
          if (result['codeNaturePersonne'] == 'A') {
            this.designationSearch = result['designation'];
            this.typeRaisonSocialeSearch = this.raisonSocialeList[2];
          }
          if (result['codeNaturePersonne'] == 'M') {
            this.raisonSocialeSearch = result['raisonSociale'];
            this.typeRaisonSocialeSearch = this.raisonSocialeList[1];
          }
          this.choixRaisonSociale = this.typeRaisonSocialeSearch?.code;
        });
      });
    } else {
      this.action = 'Ajouter';
      this.typePrixDrowdown = this.typePrixList[0];
      let date = new Date(args['date']);
      if (new Date(args['date']).getTime() < new Date().getTime()) {
        this.dateDebut = new Date();
      } else {
        this.dateDebut = args['date'];
      }
      if (this.dateDebut.getMonth() >= 6) {
        this.dateFin = new Date(this.dateDebut.getFullYear() + 1, 11, 31)
      } else {
        this.dateFin = new Date(this.dateDebut.getFullYear(), 11, 31)
      }
      this.anterieureADateSystem = true;
      this.dateFinAnterieureADateSystem = true;
    }

    setTimeout(() => {
      this.display = true;
    }, 700);
  }

  onImmeubleReservationChanged(args: ChangeFilterReservationArg) {
    this.immeubleId = args.roomtype;
    if (args.roomtype != 0) {
      this.rooms = [];
      if (this.sub) {
        this.sub.unsubscribe();
        this.sub = undefined;
      }
      this.sub = this.http.get(apiLit + 'immeuble/' + args.roomtype).subscribe((res: Lit[]) => {
        for (let element of res) {
          if (element.isActif == true && element.chambre.isActif == true && element.chambre.immeuble.isActif == true) {
            let room = new RoomDto();
            room.roomId = element['id'];
            room.roomNumber = element['nom'];
            this.rooms.push(room);
          }
        }
        this.http.get(apiReservation + 'personneLits').subscribe((res: any[]) => {
          let resultat = res;
          for (let element of res) {
            var dateDebut = new Date(element['personneLits'][0]['dateDebut']);
            var dateFin = new Date(element['personneLits'][0]['dateFin']);
            var dateDebutString = dateDebut.getDate() + '-' + (dateDebut.getMonth() + 1) + '-' + dateDebut.getFullYear();
            var dateFinString = dateFin.getDate() + '-' + (dateFin.getMonth() + 1) + '-' + dateFin.getFullYear();
            let booking = new BookingDto();
            booking.bookingId = element['id'];
            booking.roomId = element['personneLits'][0]['lit']['id'];
            booking.boxName = element['personneLits'][0]['lit']['nom'];
            booking.startDate = new Date(element['personneLits'][0]['dateDebut']);
            booking.endDate = new Date(element['personneLits'][0]['dateFin']);
            booking.stayDay = element['personneLits'][0]['dureeHebergement'];
            booking.name = 'Réservation du : ' + dateDebutString + ' au ' + dateFinString + ' - Personne : ' + element['personneLits'][0]['personne']['nom'] + ' ' + element['personneLits'][0]['personne']['prenom'];
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
      this.sub = this.http.get(apiLit).subscribe((res: Lit[]) => {
        for (let element of res) {
          if (element.isActif == true && element.chambre.isActif == true && element.chambre.immeuble.isActif == true) {
            let room = new RoomDto();
            room.roomId = element['id'];
            room.roomNumber = element['nom'];
            this.rooms.push(room);
          }
        }

        this.http.get(apiReservation + 'personneLits').subscribe((res: any[]) => {
          let resultat = res;
          for (let element of res) {
            var dateDebut = new Date(element['personneLits'][0]['dateDebut']);
            var dateFin = new Date(element['personneLits'][0]['dateFin']);
            var dateDebutString = dateDebut.getDate() + '-' + (dateDebut.getMonth() + 1) + '-' + dateDebut.getFullYear();
            var dateFinString = dateFin.getDate() + '-' + (dateFin.getMonth() + 1) + '-' + dateFin.getFullYear();
            let booking = new BookingDto();
            booking.bookingId = element['id'];
            booking.roomId = element['personneLits'][0]['lit']['id'];
            booking.boxName = element['personneLits'][0]['lit']['nom'];
            booking.startDate = new Date(element['personneLits'][0]['dateDebut']);
            booking.endDate = new Date(element['personneLits'][0]['dateFin']);
            booking.stayDay = element['personneLits'][0]['dureeHebergement'];
            booking.name = 'Réservation du : ' + dateDebutString + ' au ' + dateFinString + ' - Personne : ' + element['personneLits'][0]['personne']['nom'] + ' ' + element['personneLits'][0]['personne']['prenom'];
            this.bookings.push(booking);
          }


        });
        this.cd.detectChanges();
      });
    }
  }

  onChambreReservationChanged(args: ChangeFilterReservationArg) {
    if (args.roomtype != 0) {
      this.rooms = [];
      if (this.sub) {
        this.sub.unsubscribe();
        this.sub = undefined;
      }
      this.sub = this.http.get(apiLit + 'chambre/' + args.roomtype).subscribe((res: Lit[]) => {
        for (let element of res) {
          if (element.isActif == true && element.chambre.isActif == true && element.chambre.immeuble.isActif == true) {
            let room = new RoomDto();
            room.roomId = element['id'];
            room.roomNumber = element['nom'];
            this.rooms.push(room);
          }
        }
        this.http.get(apiChambre + args.roomtype).subscribe(res => {
          this.room = new RoomDto();
          this.room.roomId = res['id'];
          this.room.roomNumber = res['nom'];
        });
        this.http.get(apiReservation + 'personneLits').subscribe((res: any[]) => {
          let resultat = res;
          for (let element of res) {
            var dateDebut = new Date(element['personneLits'][0]['dateDebut']);
            var dateFin = new Date(element['personneLits'][0]['dateFin']);
            var dateDebutString = dateDebut.getDate() + '-' + (dateDebut.getMonth() + 1) + '-' + dateDebut.getFullYear();
            var dateFinString = dateFin.getDate() + '-' + (dateFin.getMonth() + 1) + '-' + dateFin.getFullYear();
            let booking = new BookingDto();
            booking.bookingId = element['id'];
            booking.roomId = element['personneLits'][0]['lit']['id'];
            booking.boxName = element['personneLits'][0]['lit']['nom'];
            booking.litName = element['personneLits'][0]['lit']['nom'];
            booking.startDate = new Date(element['personneLits'][0]['dateDebut']);
            booking.endDate = new Date(element['personneLits'][0]['dateFin']);
            booking.stayDay = element['personneLits'][0]['dureeHebergement'];
            booking.name = 'Réservation du : ' + dateDebutString + ' au ' + dateFinString + ' - Personne : ' + element['personneLits'][0]['personne']['nom'] + ' ' + element['personneLits'][0]['personne']['prenom'];
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
      this.sub = this.http.get(apiLit + 'immeuble/' + this.immeubleId).subscribe((res: Lit[]) => {
        for (let element of res) {
          if (element.isActif == true && element.chambre.isActif == true && element.chambre.immeuble.isActif == true) {
            let room = new RoomDto();
            room.roomId = element['id'];
            room.roomNumber = element['nom'];
            this.rooms.push(room);
          }
        }
        this.http.get(apiReservation + 'personneLits').subscribe((res: any[]) => {
          let resultat = res;
          for (let element of res) {
            var dateDebut = new Date(element['personneLits'][0]['dateDebut']);
            var dateFin = new Date(element['personneLits'][0]['dateFin']);
            var dateDebutString = dateDebut.getDate() + '-' + (dateDebut.getMonth() + 1) + '-' + dateDebut.getFullYear();
            var dateFinString = dateFin.getDate() + '-' + (dateFin.getMonth() + 1) + '-' + dateFin.getFullYear();
            let booking = new BookingDto();
            booking.bookingId = element['id'];
            booking.roomId = element['personneLits'][0]['lit']['id'];
            booking.boxName = element['personneLits'][0]['lit']['nom'];
            booking.litName = element['personneLits'][0]['lit']['nom'];
            booking.startDate = new Date(element['personneLits'][0]['dateDebut']);
            booking.endDate = new Date(element['personneLits'][0]['dateFin']);
            booking.stayDay = element['personneLits'][0]['dureeHebergement'];
            booking.name = 'Réservation du : ' + dateDebutString + ' au ' + dateFinString + ' - Personne : ' + element['personneLits'][0]['personne']['nom'] + ' ' + element['personneLits'][0]['personne']['prenom'];
            this.bookings.push(booking);
          }


        });
        this.cd.detectChanges();
      });
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
      };

      this.http.search(apiPersonne + 'search2', params).subscribe(
        (res: any[]) => {

          this.personneAFacturers = res;
          if (this.personneAFacturers.length == 1) {
            this.personneAFacturer = this.personneAFacturers[0];
            this.nomSearch = this.personneAFacturers[0]['nom'];
            this.prenomSearch = this.personneAFacturers[0]['prenom'];
            this.designationSearch = this.personneAFacturers[0]['designation'];
            this.raisonSocialeSearch = this.personneAFacturers[0]['raisonSociale'];
            this.cinSearch = this.personneAFacturers[0]['numeroPieceIdentite'];
            this.personneAFacturerPersonneId = this.personneAFacturers[0]['id'];
          } else if (this.personneAFacturers.length > 1) {
            this.personneSelectionDisplay = true;
          } else {
            this.messageService.add({severity: 'error', summary: 'Erreur', detail: 'Aucune personne trouvée !'});
          }
        }
      );
    }
  }

  doPersonneSearch() {
    if (this.nomPersonneSearch.length == 0 && this.prenomPersonneSearch.length == 0 && this.cinPersonneSearch.length == 0) {
      this.messageService.add({severity: 'warn', summary: 'Attention', detail: 'Merci de saisir une valeur pour la recherche'});
    } else {
      let params = {
        nom: this.nomPersonneSearch,
        prenom: this.prenomPersonneSearch,
        cin: this.cinPersonneSearch,
        designation: '',
        raisonSociale: ''
      };

      this.http.search(apiPersonne + 'search3', params).subscribe(
        (res: any[]) => {
          this.personnes = res;
          if (this.personnes.length == 1) {
            this.personne = this.personnes[0];
            this.nomPersonneSearch = this.personnes[0]['nom'] ? this.personnes[0]['nom'] : '';
            this.prenomPersonneSearch = this.personnes[0]['prenom'] ? this.personnes[0]['prenom'] : '';
            this.designationSearch = this.personnes[0]['designation'] ? this.personnes[0]['designation'] : '';
            this.cinPersonneSearch = this.personnes[0]['numeroPieceIdentite'] ? this.personnes[0]['numeroPieceIdentite'] : '';
            this.raisonSocialeSearch = this.personnes[0]['raisonSociale'] ? this.personnes[0]['raisonSociale'] : '';
            this.http.get(apiPersonne + '/employeur/' + this.personnes[0]['id']).subscribe(resu => {
              this.personneAFacturer = resu;
              this.personneAFacturerPersonneId = resu['id'];
              if (resu['codeNaturePersonne'] == 'P') {
                this.choixRaisonSociale = 'P';
                this.typeRaisonSocialeSearch = this.raisonSocialeList[0];
                this.nomEmployeur = resu['nom'] + ' ' + resu['prenom'];
                this.nomSearch = resu['nom'];
                this.prenomSearch = resu['prenom'];
                this.cinSearch = resu['numeroPieceIdentite'];
              }
              if (resu['codeNaturePersonne'] == 'M') {
                this.choixRaisonSociale = 'M';
                this.typeRaisonSocialeSearch = this.raisonSocialeList[1];
                this.nomEmployeur = resu['raisonSociale'];
                this.raisonSocialeSearch = resu['raisonSociale'];
              }
              if (resu['codeNaturePersonne'] == 'A') {
                this.choixRaisonSociale = 'A';
                this.typeRaisonSocialeSearch = this.raisonSocialeList[2];
                this.nomEmployeur = resu['designation'];
                this.designationSearch = resu['designation'];
              }
            });
          } else if (this.personnes.length > 1) {
            this.selectionDisplay = true;
          } else {
            this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Aucune personne trouvée !"});
          }
        }
      );
    }
  }

  onImmeubleChoosen(event) {
    if (event.value != null) {
      this.immeuble = event.value;
      this.http.get(apiChambre + 'immeuble/' + this.immeuble['id']).subscribe(
        (res: any[]) => {
          this.chambres = res;
        }
      );
    } else {
      this.chambres = [];
    }
  }

  onChambreChoosen(event) {
    if (event.value != null) {
      this.chambre = event.value;
      this.http.get(apiLit + 'chambre/' + this.chambre['id']).subscribe(
        (res: any[]) => {
          this.lits = res;
        }
      );
    } else {
      this.lits = [];
    }
  }

  onDateDebutChange(event) {
    let dateChoisie = new Date(event);
    this.dateFin = new Date(dateChoisie.getFullYear() + "-12-31");
    this.minDateFin = new Date(dateChoisie.getTime() + (1000 * 60 * 60 * 24));
  }

  onDateFinChange(event) {

  }

  onRaisonSocialeChange(event) {
    this.raisonSocialeCatched = event.value;
    this.choixRaisonSociale = this.raisonSocialeCatched?.code;
    this.nomSearch = '';
    this.prenomSearch = '';
    this.cinSearch = '';
    this.designationSearch = '';
    this.raisonSocialeSearch = '';
    // this.resetPAFSearchInputs();
  }

  resetPAFSearchInputs() {
    this.nomSearch = '';
    this.prenomSearch = '';
    this.cinSearch = '';
    this.designationSearch = '';
    this.raisonSocialeSearch = '';
    this.typeRaisonSocialeSearch = [];
    this.raisonSocialeCatched = new RaisonSociale();
    this.choixRaisonSociale = '';
  }

  resetSearchInputs() {
    this.nomPersonneSearch = '';
    this.prenomPersonneSearch = '';
    this.cinPersonneSearch = '';
    this.nomEmployeur = '';
  }

  closeReservationDialog() {
    this.display = false;
    this.enCours = false;
    this.resetSearchInputs();
    this.resetPAFSearchInputs();
    this.personne = undefined;
    this.personneAFacturer = undefined;
    this.anterieureADateSystem = false;
    this.dateFinAnterieureADateSystem = false;
  }

  saveReservation() {
    if (this.action == 'Ajouter') {
      if (this.personneAFacturer == null || this.personneAFacturer == null) {
        this.messageService.add({severity: 'warn', summary: 'Attention', detail: 'Merci de remplir tous les champs nécessaires.'});
      } else {
        let params = {
          dateDebut: new Date(this.dateDebut),
          dateFin: new Date(this.dateFin),
          idPersonneAFacturer: this.personneAFacturer['id'],
          idPersonne: this.personne['id'],
          idBox: '0',
          idPiste: '0',
          idLit: this.litDropdown['id'],
          idCheval: '0',
          typePrix: this.typePrixDrowdown['code'],
          typeReservation: 'PL',
          idSession: this.sessionService.getItem('currentUser')['idSession']
        };

        this.http.postWithParams(apiReservation + 'creer', params).subscribe(res => {
          this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Réservation créée !'});
          setTimeout(() => {
            this.closeReservationDialog();
            this.ngOnInit();
          }, 1000);
        }, error => {
          this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Cette personne a déjà une réservation durant la période sélectionnée."});
        });
      }
    } else {
      let params = {
        idReservation: this.bookingEdit['booking']['bookingId'],
        dateDebut: new Date(this.dateDebut),
        dateFin: new Date(this.dateFin),
        idPersonneAFacturer: this.personneAFacturerPersonneId,
        idPersonne: (this.nomPersonneSearch != '' && this.prenomPersonneSearch != '' && this.cinPersonneSearch != '') ? this.personne['id'] : 0,
        idBox: '0',
        idPiste: '0',
        idLit: this.litDropdown['id'],
        idCheval: '0',
        typePrix: this.typePrixDrowdown['code'],
        typeReservation: 'PL',
        idSession: this.sessionService.getItem('currentUser')['idSession']
      };

      this.http.postWithParams(apiReservation + 'modifier', params).subscribe(res => {
        this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Réservation modifiée !'});
        setTimeout(() => {
          this.closeReservationDialog();
          this.ngOnInit();
        }, 1000);
      }, error => {
        this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Il n'est pas possible de modifier cette réservation."});
      });
    }
  }

  deleteReservation() {
    this.confirmationService.confirm({
      message: 'Etes-vous sûr de vouloir supprimer la réservation ?',
      accept: () => {
        this.http.delete(apiReservation, this.bookingEdit['booking']['bookingId']).subscribe(res => {
          this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Réservation supprimée !'});
          setTimeout(() => {
            this.closeReservationDialog();
            this.ngOnInit();
          }, 1000);
        }, error => {
          this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Il n'est pas possible de supprimer cette réservation."});
        });
      }
    });
  }

  selectionner(personne: any) {
    this.personne = personne;
    this.nomPersonneSearch = personne.nom;
    this.prenomPersonneSearch = personne?.prenom;
    this.cinPersonneSearch = personne?.numeroPieceIdentite;
    this.personneId = personne?.id;

    this.http.get(apiPersonne + '/employeur/' + this.personneId).subscribe(resu => {
      this.personneAFacturer = resu;
      this.personneAFacturerPersonneId = resu['id'];
      if (resu['codeNaturePersonne'] == 'P') {
        this.choixRaisonSociale = 'P';
        this.typeRaisonSocialeSearch = this.raisonSocialeList[0];
        this.nomEmployeur = resu['nom'] + ' ' + resu['prenom'];
        this.nomSearch = resu['nom'];
        this.prenomSearch = resu['prenom'];
        this.cinSearch = resu['numeroPieceIdentite'];
      }
      if (resu['codeNaturePersonne'] == 'M') {
        this.choixRaisonSociale = 'M';
        this.typeRaisonSocialeSearch = this.raisonSocialeList[1];
        this.nomEmployeur = resu['raisonSociale'];
        this.raisonSocialeSearch = resu['raisonSociale'];
      }
      if (resu['codeNaturePersonne'] == 'A') {
        this.choixRaisonSociale = 'A';
        this.typeRaisonSocialeSearch = this.raisonSocialeList[2];
        this.nomEmployeur = resu['designation'];
        this.designationSearch = resu['designation'];
      }
    });
    this.selectionDisplay = false;
  }

  closeSelectionDialog() {
    this.selectionDisplay = false;
  }

  personneSelectionner(personneAFacturer: any) {
    this.personneAFacturer = personneAFacturer;
    this.nomSearch = personneAFacturer['nom'];
    this.prenomSearch = personneAFacturer['prenom'];
    this.designationSearch = personneAFacturer['designation'];
    this.raisonSocialeSearch = personneAFacturer['raisonSociale'];
    this.cinSearch = personneAFacturer['numeroPieceIdentite'];
    this.personneAFacturerPersonneId = personneAFacturer['id'];
    this.personneSelectionDisplay = false;
  }

  arreterReservation() {
    this.confirmationService.confirm({
      message: "Etes-vous sûr de vouloir arrêter la réservation ?",
      accept: () => {
        this.http.delete(apiReservation + "arreter/", this.bookingEdit["booking"]["bookingId"]).subscribe(res => {
          this.messageService.add({severity: 'success', summary: 'Confirmation', detail: 'Réservation arrêtée !'});
          setTimeout(() => {
            this.closeReservationDialog();
            this.ngOnInit();
          }, 1000);
        }, error => {
          this.messageService.add({severity: 'error', summary: 'Erreur', detail: "Il n'est pas possible d'arrêter cette réservation."});
        });
      }
    });
  }

  resetPersonneChoisie() {
    this.nomPersonneSearch = "";
    this.prenomPersonneSearch = "";
    this.cinPersonneSearch = "";
    this.nomEmployeur = "";
    this.personne = undefined;
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
