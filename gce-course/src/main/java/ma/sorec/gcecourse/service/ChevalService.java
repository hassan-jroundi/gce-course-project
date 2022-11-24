package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Cheval;

import java.util.List;

public interface ChevalService {

    List<Cheval> listAll();

    Cheval get(Long id);

    List<Cheval> findAllByNumeroEsrimaAndNomCheval(String numeroEsrima, String nomCheval);

    List<Cheval> findAllByNumeroEsrimaAndNomChevalAndNumeroTranspondeur(String numeroEsrima, String nomCheval, String numeroTranspondeur);

    List<Cheval> findAllByNumeroEsrimaAndNumeroTranspondeur(String numeroEsrima, String numeroTranspondeur);

    List<Cheval> findAllByNumeroEsrima(String numeroEsrima);

    List<Cheval> findAllByNumeroTranspondeur(String numeroTranspondeur);

    List<Cheval> findAllChevalAvecReservationEnCours();

}
