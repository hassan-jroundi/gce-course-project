package ma.sorec.gcecourse.authentification;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Properties;

public class LoginLDAP {

    public static void main(String[] args) throws Exception {

        String url = "ldap://ad.sorec.ma:389";
        String principalName = "hjroundi@sorec.ma";
        String domainName = "sorec.ma";

        if (domainName==null || "".equals(domainName)) {
            int delim = principalName.indexOf('@');
            domainName = principalName.substring(delim+1);
        }

        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, url);
        props.put(Context.SECURITY_PRINCIPAL, principalName);
        props.put(Context.SECURITY_CREDENTIALS, "SA2021jr"); // secretpwd
        if (url.toUpperCase().startsWith("LDAPS://")) {
            props.put(Context.SECURITY_PROTOCOL, "ssl");
            props.put(Context.SECURITY_AUTHENTICATION, "simple");
            props.put("java.naming.ldap.factory.socket", "test.DummySSLSocketFactory");
        }

        InitialDirContext context = new InitialDirContext(props);
        try {
            SearchControls ctrls = new SearchControls();
            ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> results = context.search(toDC(domainName),"(& (userPrincipalName="+principalName+")(objectClass=user))", ctrls);
            if(!results.hasMore())
                throw new AuthenticationException("Principal name not found");

            SearchResult result = results.next();

            Attribute memberOf = result.getAttributes().get("memberOf");
            if(memberOf!=null) {
                for(int idx=0; idx<memberOf.size(); idx++) {
                }
            }
        } finally {
            try { context.close(); } catch(Exception ex) {
                throw ex;
            }
        }
    }

    private static String toDC(String domainName) {
        StringBuilder buf = new StringBuilder();
        for (String token : domainName.split("\\.")) {
            if(token.length()==0) continue;
            if(buf.length()>0)  buf.append(",");
            buf.append("DC=").append(token);
        }
        return buf.toString();
    }

}
