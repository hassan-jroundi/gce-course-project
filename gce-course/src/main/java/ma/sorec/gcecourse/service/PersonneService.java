package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Personne;

import java.util.List;

public interface PersonneService {

    List<Personne> listAll();

    Personne save(Personne personne);

    Personne update(Personne personne);

    Personne get(String id);

    void delete(String id);

    List<Personne> findAllByNomAndPrenomAndNumeroPieceIdentiteAndCodeNaturePersonne(String nom, String prenom, String cin, String codeNaturePersonne);

    List<Personne> findAllByNomAndPrenomAndNumeroPieceIdentiteAndDesignationAndRaisonSociale(String nom, String prenom, String cin, String designation, String raisonSociale);

    List<Personne> findAllByCriteria(String nom, String prenom, String cin, String designation, String raisonSociale);

    List<Personne> findAllByCriteriaBis(String nom, String prenom, String cin, String designation, String raisonSociale);

    Personne findFirstByNumeroPieceIdentite(String numeroPieceIdentite);

    Personne findLADEmployeur(String idLAD);

}
