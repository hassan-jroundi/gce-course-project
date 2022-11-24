package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.ChevalReservationEtat;

import java.util.List;

public interface ChevalReservationEtatService {

    List<ChevalReservationEtat> listAll();

    ChevalReservationEtat save(ChevalReservationEtat chevalReservationEtat);

    ChevalReservationEtat get(Long id);

    void delete(Long id);

    ChevalReservationEtat getByReservationId(Long idReservation);

}
