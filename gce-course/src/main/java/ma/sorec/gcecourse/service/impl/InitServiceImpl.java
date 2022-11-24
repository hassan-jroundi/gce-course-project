package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.*;
import ma.sorec.gcecourse.repository.*;
import ma.sorec.gcecourse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@Transactional
public class InitServiceImpl implements InitService {

    @Autowired
    ImmeubleRepository immeubleService;

    @Autowired
    ChambreRepository chambreService;

    @Autowired
    LitRepository litService;

    @Autowired
    EcurieRepository ecurieService;

    @Autowired
    BoxRepository boxService;

    @Autowired
    SiteRepository siteService;

    @Autowired
    PrixLitRepository prixLitService;

    @Autowired
    PrixBoxRepository prixBoxService;

    @Autowired
    NombreLitsHistoriqueRepository nombreLitsHistoriqueService;

    @Override
    public void ajouterBatimentsAvecChambresEtLits() {

//        Site siteExistant = siteService.findFirstByNomIgnoreCase("CENTRE DE BOUZNIKA");
//
//        if (siteExistant == null) {
//            Site site = Site.builder()
//                    .nom("CENTRE DE BOUZNIKA")
//                    .ville("BOUZNIKA")
//                    .dateCreation(new Date())
//                    .build();
//            siteService.save(site);
//        }
//
//        Immeuble immeubleExistant = immeubleService.findFirstByNomIgnoreCase("BATIMENT PRINCIPAL");
//
//        if (immeubleExistant == null) {
//            Immeuble immeuble = Immeuble.builder()
//                    .isActif(true)
//                    .nom("BATIMENT PRINCIPAL")
//                    .dateCreation(new Date())
//                    .site(siteService.findFirstByNomIgnoreCase("CENTRE DE BOUZNIKA"))
//                    .build();
//
//            immeuble = immeubleService.save(immeuble);
//        }

        for (char alphabet = 'A'; alphabet <= 'T'; alphabet++) {

            Chambre chambre = Chambre.builder()
                    .immeuble(immeubleService.findFirstByNomIgnoreCase("BATIMENT PRINCIPAL"))
                    .nom("CHAMBRE " + alphabet)
                    .isActif(true)
                    .dateCreation(new Date())
                    .build();

            chambre = chambreService.save(chambre);

            PrixLit prixLitUnitaire = PrixLit.builder()
                    .typePrix("U")
                    .dateCreation(new Date())
                    .dateDebut(new Date())
                    .idChambre(chambre.getId().toString())
                    .montant(7L)
                    .build();

            prixLitService.save(prixLitUnitaire);

            PrixLit prixLitForfaitaire = PrixLit.builder()
                    .typePrix("F")
                    .dateCreation(new Date())
                    .dateDebut(new Date())
                    .idChambre(chambre.getId().toString())
                    .montant(200L)
                    .build();

            prixLitService.save(prixLitForfaitaire);

            int nombreLits = 0;

            for (int i = 1; i <= 4; i++) {

                Lit lit = Lit.builder()
                        .nom(String.valueOf(alphabet) + i)
                        .dateCreation(new Date())
                        .chambre(chambre)
                        .isActif(true)
                        .build();

                litService.save(lit);

                nombreLits++;

            }

            NombreLitsHistorique nombreLitsHistorique = NombreLitsHistorique.builder()
                    .idChambre(chambre.getId().toString())
                    .nombre((long) nombreLits)
                    .dateDebut(new Date())
                    .dateCreation(new Date())
                    .build();

            nombreLitsHistoriqueService.save(nombreLitsHistorique);
        }
    }

    @Override
    public void ajouterEcuriesAvecBoxs() {

//        Site existant = siteService.findFirstByNomIgnoreCase("CENTRE DE BOUZNIKA");
//
//        if (existant == null) {
//            Site site = Site.builder()
//                    .nom("CENTRE DE BOUZNIKA")
//                    .ville("BOUZNIKA")
//                    .dateCreation(new Date())
//                    .build();
//            siteService.save(site);
//        }

        for (char alphabet = 'A'; alphabet <= 'T'; alphabet++) {

            Ecurie ecurie = Ecurie.builder()
                    .isActif(true)
                    .dateCreation(new Date())
                    .nom("ECURIE " + alphabet)
                    .site(siteService.findFirstByNomIgnoreCase("CENTRE DE BOUZNIKA"))
                    .build();

            ecurie = ecurieService.save(ecurie);

            if (alphabet == 'E' || alphabet == 'F' || alphabet == 'O' || alphabet == 'P' ) {
                // 10
                for (int i = 1; i <= 10; i++) {

                    Box box = Box.builder()
                            .isActif(true)
                            .dateCreation(new Date())
                            .ecurie(ecurie)
                            .nom(String.valueOf(alphabet) + i)
                            .build();

                    box = boxService.save(box);

                    PrixBox prixBoxUnitaire = PrixBox.builder()
                            .idBox(box.getId().toString())
                            .dateCreation(new Date())
                            .dateDebut(new Date())
                            .typePrix("U")
                            .montant(17L)
                            .build();

                    prixBoxService.save(prixBoxUnitaire);

                    PrixBox prixBoxForfaitaire = PrixBox.builder()
                            .idBox(box.getId().toString())
                            .dateCreation(new Date())
                            .dateDebut(new Date())
                            .typePrix("F")
                            .montant(500L)
                            .build();

                    prixBoxService.save(prixBoxForfaitaire);

                }
            } else if (alphabet == 'A' || alphabet == 'D' || alphabet == 'G' || alphabet == 'J' || alphabet == 'K'|| alphabet == 'N'|| alphabet == 'Q'|| alphabet == 'T' ) {
                // 13
                for (int i = 1; i <= 13; i++) {

                    Box box = Box.builder()
                            .isActif(true)
                            .dateCreation(new Date())
                            .ecurie(ecurie)
                            .nom(String.valueOf(alphabet) + i)
                            .build();

                    box = boxService.save(box);

                    PrixBox prixBoxUnitaire = PrixBox.builder()
                            .idBox(box.getId().toString())
                            .dateCreation(new Date())
                            .dateDebut(new Date())
                            .typePrix("U")
                            .montant(17L)
                            .build();

                    prixBoxService.save(prixBoxUnitaire);

                    PrixBox prixBoxForfaitaire = PrixBox.builder()
                            .idBox(box.getId().toString())
                            .dateCreation(new Date())
                            .dateDebut(new Date())
                            .typePrix("F")
                            .montant(500L)
                            .build();

                    prixBoxService.save(prixBoxForfaitaire);

                }
            } else if (alphabet == 'B' || alphabet == 'C' || alphabet == 'H' || alphabet == 'I' || alphabet == 'L' || alphabet == 'M' || alphabet == 'R' || alphabet == 'S') {
                // 16
                for (int i = 1; i <= 16; i++) {

                    Box box = Box.builder()
                            .isActif(true)
                            .dateCreation(new Date())
                            .ecurie(ecurie)
                            .nom(String.valueOf(alphabet) + i)
                            .build();

                    box = boxService.save(box);

                    PrixBox prixBoxUnitaire = PrixBox.builder()
                            .idBox(box.getId().toString())
                            .dateCreation(new Date())
                            .dateDebut(new Date())
                            .typePrix("U")
                            .montant(17L)
                            .build();

                    prixBoxService.save(prixBoxUnitaire);

                    PrixBox prixBoxForfaitaire = PrixBox.builder()
                            .idBox(box.getId().toString())
                            .dateCreation(new Date())
                            .dateDebut(new Date())
                            .typePrix("F")
                            .montant(500L)
                            .build();

                    prixBoxService.save(prixBoxForfaitaire);

                }
            }
        }

    }
}
