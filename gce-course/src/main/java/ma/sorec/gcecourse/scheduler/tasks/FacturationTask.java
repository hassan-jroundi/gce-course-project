package ma.sorec.gcecourse.scheduler.tasks;

import ma.sorec.gcecourse.service.FactureService;
import ma.sorec.gcecourse.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FacturationTask {

    @Autowired
    ReservationService reservationService;

    @Autowired
    FactureService factureService;

    //    @Scheduled(cron = "0 0 18 L * ?")
//    @Scheduled(cron = "00 48 10 2 NOV ?")
//    public void scheduleFixedDelayTask() {
//        List<PersonneAFacturer> personneAFacturers = personneAFacturerService.listAll();
//        for (PersonneAFacturer personneAFacturer : personneAFacturers) {
//            List<Reservation> reservations = reservationService.getAllByPersonneAFacturerId(personneAFacturer.getPersonne().getId());
//            reservations.removeIf(element -> element.getFacture() == null);
//            if (reservations.size() > 0) {
//                logger.info("La personne {} a des réservations à facturer.", personneAFacturer.getPersonne().getId());
//            } else {
//                switch (personneAFacturer.getPersonne().getCodeNaturePersonne()) {
//                    case "P":
//                        logger.info("La personne {} n'a aucune réservation à facturer.", personneAFacturer.getPersonne().getNom());
//                        break;
//                    case "A":
//                        logger.info("L'association {} n'a aucune réservation à facturer.", personneAFacturer.getPersonne().getDesignation());
//                        break;
//                    case "M":
//                        logger.info("La société {} n'a aucune réservation à facturer.", personneAFacturer.getPersonne().getRaisonSociale());
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }
//
//
////        logger.info("task facturation");
////        System.out.println(
////                "Fixed delay task - " + System.currentTimeMillis() / 1000);
//    }
}
