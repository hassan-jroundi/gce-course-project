package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.Utils.TrippleDes;
import ma.sorec.gcecourse.data.Utilisateur;
import ma.sorec.gcecourse.data.UtilisateurSession;
import ma.sorec.gcecourse.exceptions.CustomException;
import ma.sorec.gcecourse.service.LoginService;
import ma.sorec.gcecourse.service.UtilisateurService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Properties;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private UtilisateurSessionService utilisateurSessionService;

    @Override
    public Utilisateur login(String login, String motDePasse) throws Exception {
        Utilisateur utilisateur = utilisateurService.getByLogin(login);
        if (utilisateur == null) {
            throw new CustomException("A", "Utilisateur incorrect");
        } else if (utilisateur.getCodeEtat().equals("D")) {
           throw new CustomException("A", "Utilisateur désactivé");
        } else {
            if (utilisateur.getCodeTypeUtilisateur().equals("E")) {
                String decrytpedMotDePasse = TrippleDes.decrypt(utilisateur.getMotDePasse());
                if (motDePasse.equals(decrytpedMotDePasse)) {
                    // Login Externe successful
                    UtilisateurSession utilisateurSession = UtilisateurSession.builder()
                            .utilisateur(utilisateur)
                            .dateCreation(new Date())
                            .userCreation(utilisateur.getId())
                            .dateDebut(new Date())
                            .idSession(SharedUtil.getToken())
                            .build();
                    utilisateurSessionService.save(utilisateurSession);
                    return utilisateurService.ajouterUtilisateurSiteEtProfilEtSession(utilisateur.getId());
                } else {
                    throw new CustomException("A", "Mot de passe incorrect");
                }
            }
            if (utilisateur.getCodeTypeUtilisateur().equals("I")) {
                if (this.verifierCompteAD(login, motDePasse)) {
                    // Login Interne successful
                    UtilisateurSession utilisateurSession = UtilisateurSession.builder()
                            .utilisateur(utilisateur)
                            .dateCreation(new Date())
                            .userCreation(utilisateur.getId())
                            .dateDebut(new Date())
                            .idSession(SharedUtil.getToken())
                            .build();
                    utilisateurSessionService.save(utilisateurSession);
                    return utilisateurService.ajouterUtilisateurSiteEtProfilEtSession(utilisateur.getId());
                } else {
                    throw new CustomException("A", "Mot de passe incorrect");
                }
            }
            return null;
        }
    }

    @Override
    public boolean verifierCompteAD(String login, String motDePasse) throws NamingException {
        String url = "ldap://ad.sorec.ma:389"; // ldap://1.2.3.4:389 or ldaps://1.2.3.4:636
        String principalName = login + "@sorec.ma"; // firstname.lastname@mydomain.com
        String domainName = "sorec.ma"; // mydomain.com or empty

        if ("".equals(domainName)) {
            int delim = principalName.indexOf('@');
            domainName = principalName.substring(delim+1);
        }

        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, url);
        props.put(Context.SECURITY_PRINCIPAL, principalName);
        props.put(Context.SECURITY_CREDENTIALS, motDePasse); // secretpwd
        if (url.toUpperCase().startsWith("LDAPS://")) {
            props.put(Context.SECURITY_PROTOCOL, "ssl");
            props.put(Context.SECURITY_AUTHENTICATION, "simple");
            props.put("java.naming.ldap.factory.socket", "test.DummySSLSocketFactory");
        }

        InitialDirContext context = null;
        try {
            context = new InitialDirContext(props);
        } catch (AuthenticationException e) {
            throw new CustomException("A", "Mot de passe incorrect");
        }
        try {
            SearchControls ctrls = new SearchControls();
            ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> results = context.search(SharedUtil.toDC(domainName),"(& (userPrincipalName="+principalName+")(objectClass=user))", ctrls);
            if(!results.hasMore())
                throw new AuthenticationException("Principal name not found");

            SearchResult result = results.next();
            log.info("distinguisedName: {}", result.getNameInNamespace());

            Attribute memberOf = result.getAttributes().get("memberOf");
            if(memberOf!=null) {
                for(int idx=0; idx<memberOf.size(); idx++) {
                    log.info("membre of : {}", memberOf.get(idx).toString());
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try { context.close(); } catch(Exception ex) {
                throw ex;
            }
        }
    }

    @Override
    public boolean seDeconnecter(String idUtilisateur) {

        Utilisateur utilisateur = utilisateurService.get(new Long(idUtilisateur));

        UtilisateurSession utilisateurSessionAncien = utilisateurSessionService.getByUtilisateurId(utilisateur.getId());
        utilisateurSessionAncien.setDateFin(new Date());
        utilisateurSessionAncien.setDateModification(new Date());
        utilisateurSessionAncien.setUserModification(utilisateur.getId());
        utilisateurSessionService.save(utilisateurSessionAncien);

        return true;
    }
}
