/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bluepot.pkgController;

import bluepot.pkgModel.Config.Settings;
import bluepot.pkgModel.Model;


import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Level;

/**
 *
 * @author andrew
 */
public class EmailFile {

    String filename;
    Settings settings;
    private Session session;
    Model model;
    
    public EmailFile(Model model) {

        this.model = model;
    }

    //TODO: REFACTOR REFACTOR REFACTOR
    public void sendEmail(String filename, Settings settings)
    {
        this.settings = settings;
        this.filename = filename;
        
            String host = settings.getSmtpHost();
            String port = Integer.toString(settings.getSmtpPort());
            String from = settings.getEmailFrom();
            String to = settings.getEmailTo();
            String subject = settings.getEmailSubject();
            String msgText1 = "";

            String username = settings.getSmtpUser();
            String password = settings.getSmtpPassword();


            Properties props = System.getProperties();
            
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            if(username.isEmpty())
            {
             props.put("mail.smtp.auth", "false");
             session = Session.getDefaultInstance(props);


            }
            else
            {
             props.put("mail.smtp.auth", "true");
             Authenticator auth = new SMTPAuthenticator();
             session = Session.getDefaultInstance(props, auth);
            }

            MimeMessage msg = new MimeMessage(session);

            session.setDebug(false);
            
            try {
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            // create and fill the first message part
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(msgText1);
            // create the second message part
            MimeBodyPart mbp2 = new MimeBodyPart();
            // attach the file to the message
            FileDataSource fds = new FileDataSource(filename);
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(fds.getName());
            // create the Multipart and add its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);
            // add the Multipart to the message
            msg.setContent(mp);
            // set the Date: header
            msg.setSentDate(new Date());
            // send the message
            Transport.send(msg);
        } catch (MessagingException ex) {
            model.log(Level.ERROR, "Error sending email, probably a configuration error: " + ex.toString());
        }

    }

    private class SMTPAuthenticator extends Authenticator
    {

        @Override
    public PasswordAuthentication getPasswordAuthentication()
    {
       String username=settings.getSmtpUser();
      String password=new String(settings.getSmtpPassword());
        return new PasswordAuthentication(username, password);
    }
}



}



