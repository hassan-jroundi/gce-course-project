package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Utilisateur;

import java.util.List;

public interface UtilisateurService {

    List<Utilisateur> listAll();

    Utilisateur save(Utilisateur utilisateur);

    Utilisateur get(Long id);

    void delete(Long id);

    List<Utilisateur> chargerListeUtilisateurs();

    Utilisateur creerUtilisateur(Utilisateur utilisateur) throws Exception;

    Utilisateur getByLogin(String login);

    Utilisateur ajouterUtilisateurSiteEtProfilEtSession(Long id);

    void supprimerUtilisateur(Long id);

    Utilisateur modifierUtilisateur(Utilisateur utilisateur);

    void changerMotDePasse(Long id, String motDePasse) throws Exception;

}
