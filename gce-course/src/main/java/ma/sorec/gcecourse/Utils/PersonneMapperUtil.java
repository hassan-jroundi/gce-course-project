package ma.sorec.gcecourse.Utils;

import ma.sorec.gcecourse.data.Personne;
import ma.sorec.gcecourse.data.mohr.PersonneM;

import java.util.ArrayList;
import java.util.List;

public class PersonneMapperUtil {

    public static Personne fromPersonneMEntityToPersonneEntity(PersonneM personneM) {

        return Personne.builder()
                .codeNaturePersonne(personneM.getCodeNaturePersonne())
                .codeTitrePersonne(personneM.getCodeTitrePersonne())
                .adresse1(personneM.getAdresse1())
                .adresse2(personneM.getAdresse2())
                .categoriePersMora(personneM.getCategoriePersMora())
                .codeCategorie(personneM.getCodeCategorie())
                .codePaysOrigine(personneM.getCodePaysOrigine())
                .codeSexe(personneM.getCodeSexe())
                .codeTypePieceIdentite(personneM.getCodeTypePieceIdentite())
                .codeVille1(personneM.getCodeVille1())
                .dateCreation(personneM.getDateCreation())
                .dateNaissance(personneM.getDateNaissance())
                .designation(personneM.getDesignation())
                .email(personneM.getEmail())
                .etrangere(personneM.getEtrangere())
                .id(personneM.getId())
                .idBanque(personneM.getIdBanque())
                .nom(personneM.getNom())
                .nomGerant(personneM.getNomGerant())
                .numeroPieceIdentite(personneM.getNumeroPieceIdentite())
                .numeroTelephone1(personneM.getNumeroTelephone1())
                .numeroTelephone2(personneM.getNumeroTelephone2())
                .prenom(personneM.getPrenom())
                .raisonSociale(personneM.getRaisonSociale())
                .rib(personneM.getRib())
                .databaseSource("M")
                .build();
    }

    public static List<Personne> fromPersonneMEntitiesToPersonneEntities(List<PersonneM> personneMList) {

        List<Personne> personnes = new ArrayList<>();

        for (PersonneM personneM : personneMList) {
            personnes.add(fromPersonneMEntityToPersonneEntity(personneM));
        }

        return personnes;
    }
}
