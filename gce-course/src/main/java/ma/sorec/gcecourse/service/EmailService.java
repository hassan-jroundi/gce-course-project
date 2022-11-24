package ma.sorec.gcecourse.service;

import com.itextpdf.text.DocumentException;
import ma.sorec.gcecourse.data.Facture;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

    void envoyerMailFacture(Facture facture, String idSession) throws IOException, DocumentException, MessagingException;

    void envoyerMailDeRelance(Facture facture, String idSession) throws IOException, DocumentException, MessagingException;

    void envoyerMail(Facture facture, String type, String idSession) throws IOException, DocumentException, MessagingException;

}
