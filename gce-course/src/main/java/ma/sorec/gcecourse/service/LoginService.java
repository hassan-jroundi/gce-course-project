package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Utilisateur;

import javax.naming.NamingException;

public interface LoginService {

    Utilisateur login(String login, String motDePasse) throws Exception;

    boolean verifierCompteAD(String login, String motDePasse) throws NamingException;

    boolean seDeconnecter(String idUtilisateur);

}
