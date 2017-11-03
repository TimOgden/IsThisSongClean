
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Tim
 */
public class EmailSender {

    public static void sendEmail(String title, String artist, String source) {
        // Recipient's email ID needs to be mentioned.
        String to = "togden1999@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "togden1@binghamton.edu";

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Possible discrepancy in IsThisSongClean!");

            // Now set the actual message
            message.setText("Someone has reported that " + title + " by " + artist + " is not correct. "
                    + "You should check it out here:" + "\nSource: " + source + "");

            // Send message
            Transport.send(message);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
