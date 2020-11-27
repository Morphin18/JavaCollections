package com.javarush.task.task40.task4003;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/* 
Отправка письма с файлом
*/

public class Solution {

    public static void main(String[] args) throws UnsupportedEncodingException {
        Solution solution = new Solution();
        solution.sendMail("name.lastname@gmail.com", "password", "friend@gmail.com");
    }

    public static void setSubject(Message message, String subject) throws MessagingException {
        message.setSubject(subject);
    }

    public static void setAttachment(Message message, String filename) throws MessagingException, UnsupportedEncodingException {
        MimeMultipart mimeMultipart = new MimeMultipart();
        MimeBodyPart bodyPart = new MimeBodyPart();
        FileDataSource fileDataSource = new FileDataSource(filename);

        bodyPart.setFileName(MimeUtility.encodeText(fileDataSource.getName()));
        bodyPart.setDataHandler(new DataHandler(fileDataSource));
        mimeMultipart.addBodyPart(bodyPart);
        message.setContent(mimeMultipart);

    }

    public void sendMail(final String username, final String password, final String recipients) throws UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));

            setSubject(message, "Тестовое письмо");
            setAttachment(message, "c:/text.txt");

            Transport.send(message);
            System.out.println("Письмо было отправлено.");

        } catch (MessagingException e) {
            System.out.println("Ошибка при отправке: " + e.toString());
        }
    }
}