package sg.activewealth.roboadvisor.infra.helper;

/*
 * Some SMTP servers require a username and password authentication before you
 * can use their Server for Sending mail. This is most common with couple
 * of ISP's who provide SMTP Address to Send Mail.
 *
 * This Program gives any example on how to do SMTP Authentication
 * (User and Password verification)
 *
 * This is a free source code and is provided as it is without any warranties and
 * it can be used in any your code for free.
 *
 * Author : Sudhir Ancha
 */

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import sg.activewealth.roboadvisor.common.model.User;
import sg.activewealth.roboadvisor.infra.controller.AbstractController;
import sg.activewealth.roboadvisor.infra.exception.SystemException;

@Component
public class MailSenderHelper extends AbstractHelper {

    protected Logger logger = Logger.getLogger(MailSenderHelper.class);

    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat(AbstractController.DATE_TIME_FORMAT_FOR_PRINT);

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @PreDestroy
    private void distroy() {
        executorService.shutdown();
    }

    public String sendMailToUser(final String subject, final String recipients[],
            final String message, final boolean isHtml, final User user, boolean log) {
        // logger.info("sending email to: " + Arrays.asList(recipients).toString() + ", " +
        // "subject: " + subject + ((log)?", content: " + message:""));
        logger.info("sending email to: " + Arrays.asList(recipients).toString() + " on "
                + dateFormat.format(new Date()) + ", subject: " + subject + ", content: "
                + message);

        if (propertiesHelper.appIsSendEmail) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    doSendMail(subject, recipients, message, isHtml);
                }
            });
        }

        return "sending email to: " + Arrays.asList(recipients).toString() + " on "
                + dateFormat.format(new Date()) + ", subject: " + subject + ", content: " + message;
    }

    public String sendMailToUser(String subject, String recipients[], String message,
            boolean isHtml, User user) {
        return sendMailToUser(subject, recipients, message, isHtml, user, true);
    }

    public String sendMail(String subject, String recipients[], String message, boolean isHtml,
            boolean log) {
        return sendMailToUser(subject, recipients, message, isHtml, null, log);

    }

    public String sendMail(String subject, String recipients[], String message, boolean isHtml) {
        return sendMailToUser(subject, recipients, message, isHtml, null, true);
    }

    private void doSendMail(String subject, String recipients[], String message, boolean isHtml) {
        if (message == null) {
            message = "";
        }

        boolean debug = true;

        // Set the host smtp address
        Properties props = new Properties();
        props.put("mail.smtp.host", propertiesHelper.mailHost);
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.port", propertiesHelper.mailPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        // props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", propertiesHelper.mailPort);
        props.put("mail.smtp.submitter", propertiesHelper.mailBounce);

        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getInstance(props, auth);
        session.setDebug(debug);

        // create a message
        Message msg = new MimeMessage(session);
        try {
            // set the from and to address
            InternetAddress addressFrom = new InternetAddress(propertiesHelper.mailFrom);
            addressFrom.setPersonal(propertiesHelper.mailFromName);
            msg.setFrom(addressFrom);

            InternetAddress[] addressTo = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                addressTo[i] = new InternetAddress(recipients[i]);
            }

            if (recipients.length == 1) {
                msg.setRecipients(Message.RecipientType.TO, addressTo);
            } else {
                msg.setRecipients(Message.RecipientType.BCC, addressTo);
            }

            // Setting the Subject and Content Type
            msg.setSubject(subject);
            String type = isHtml ? "text/html; charset=utf-8" : "text/plain; charset=utf-8";
            msg.setContent(message, type); // or text/html
            Transport.send(msg);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    /**
     * SimpleAuthenticator is used to do simple authentication when the SMTP server requires it.
     */
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = propertiesHelper.mailUsername;
            String password = propertiesHelper.mailPassword;
            return new PasswordAuthentication(username, password);
        }
    }

}
