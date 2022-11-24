package ma.sorec.gcecourse.service.impl;

import com.itextpdf.text.DocumentException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import ma.sorec.gcecourse.Utils.DateUtil;
import ma.sorec.gcecourse.controller.mohr.PersonneMController;
import ma.sorec.gcecourse.data.Facture;
import ma.sorec.gcecourse.data.RelanceFacturation;
import ma.sorec.gcecourse.data.mohr.PersonneM;
import ma.sorec.gcecourse.service.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String NOREPLY_ADDRESS = "sorechassan@gmail.com";

    @Autowired
    PersonneMController personneMController;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    ReportService reportService;

    @Autowired
    PersonneService personneService;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SimpleMailMessage template;

    @Autowired
    private RelanceFacturationService relanceFacturationService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Value("classpath:/mail-logo.png")
    private Resource resourceFile;

    @Value("${mail.template.facturation.corps1}")
    private String facturationCorps1;

    @Value("${mail.template.facturation.corps2}")
    private String facturationCorps2;

    @Value("${mail.template.facturation.corps3}")
    private String facturationCorps3;

    @Value("${mail.template.relance.corps1}")
    private String relanceCorps1;

    @Value("${mail.template.relance.corps2}")
    private String relanceCorps2;

    @Value("${mail.template.relance.corps3}")
    private String relanceCorps3;

    @Value("${sendgrid.apikey}")
    private String apiKey;

    @Override
    public void envoyerMailFacture(Facture facture, String idSession) throws IOException, DocumentException, MessagingException {

        envoyerMail(facture, "F", idSession);
    }

    @Override
    public void envoyerMailDeRelance(Facture facture, String idSession) throws IOException, DocumentException, MessagingException {

        envoyerMail(facture, "R", idSession);
    }

    @Override
    public void envoyerMail(Facture facture, String type, String idSession) throws IOException, DocumentException, MessagingException {

        PersonneM personneAFacturer = personneMController.get(facture.getIdPersonneAFacturer());

        if (personneAFacturer.getEmail() == null) {
            logger.info("Personne à facturer n'a pas d'adresse mail déclarée ! ");
        } else {
            reportService.creerFacture(facture.getId(), true);

            String fileName = "facture-numero-" + facture.getId() + ".pdf";
            Path file = Paths.get(fileName);
            Attachments attachments = new Attachments();
            attachments.setFilename(file.getFileName().toString());
            attachments.setType("application/pdf");
            attachments.setDisposition("attachment");
            Email from = new Email();
            from.setEmail("gce@sorec.ma");

            Personalization personalization = new Personalization();

            Email to = new Email();
            to.setEmail(personneAFacturer.getEmail());
            personalization.addTo(to);

            Email cc = new Email();

            if (idSession.length() == 0) {
                cc.setEmail(utilisateurService.get(new Long(facture.getIdOperateur())).getPersonne().getEmail());
            } else {
                cc.setEmail(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getPersonne().getEmail());
            }

            personalization.addCc(cc);

            Mail mail = new Mail();
            mail.setFrom(from);

            switch (type) {
                case "F": {

                    String subject = "Facturation";
                    mail.setSubject(subject);
                    Content content = new Content("text/plain", facturationCorps1 + facturationCorps2 + facturationCorps3);
                    mail.addContent(content);
                    mail.addPersonalization(personalization);
                    break;
                }
                case "R": {

                    String subject = "Relance";
                    mail.setSubject(subject);
                    Content content = new Content("text/plain", relanceCorps1 + facture.getId() + " " + relanceCorps2 + relanceCorps3);
                    mail.addContent(content);
                    mail.addHeader("Disposition-Notification-To", "gce@sorec.ma");
                    mail.addPersonalization(personalization);
                    break;
                }
                default:
                    break;
            }

            byte[] attachmentContentBytes = Files.readAllBytes(file);
            String attachmentContent = Base64.getMimeEncoder().encodeToString(attachmentContentBytes);
            String s = Base64.getEncoder().encodeToString(attachmentContentBytes);
            attachments.setContent(s);
            mail.addAttachments(attachments);

            SendGrid sg = new SendGrid(apiKey);
            Request request = new Request();
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());
                Response response = sg.api(request);
                if (type.equals("R")) {
                    RelanceFacturation relanceFacturation = RelanceFacturation.builder()
                            .dateCreation(new Date())
                            .idFacture(facture.getId().toString())
                            .build();
                    relanceFacturationService.save(relanceFacturation);
                }
            } catch (IOException ex) {
                throw ex;
            } finally {
                Path path = Paths.get(fileName);
                Files.deleteIfExists(path);
            }
        }
    }
}
