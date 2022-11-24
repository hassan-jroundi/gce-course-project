package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.NombreLitsHistorique;

import java.util.List;

public interface NombreLitsHistoriqueService {

    List<NombreLitsHistorique> listAll();

    NombreLitsHistorique save(NombreLitsHistorique nombreLitsHistorique);

    NombreLitsHistorique get(Long id);

    void delete(Long id);

    List<NombreLitsHistorique> getAllByIdChambre(String idChambre);

    NombreLitsHistorique getLastNombreLitsHistorique(String idChambre);

}
