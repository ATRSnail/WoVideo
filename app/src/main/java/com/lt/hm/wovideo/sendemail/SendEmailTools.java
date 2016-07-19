package com.lt.hm.wovideo.sendemail;

import android.util.Log;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

/**
 * Created by songchenyu on 16/7/19.
 */

public class SendEmailTools {

    public static void send_mail_file(String str_to_mail, String str_from_mail,
                                      String str_smtp, String str_user, String str_pass,
                                      String str_title, String str_body, String str_file_path) {
        Log.v("lengfeng", "send_mail_file");
        String host = str_smtp; // 发件人使用发邮件的电子信箱服务器
        String from = str_from_mail; // 发邮件的出发地（发件人的信箱）
        String to = str_to_mail; // 发邮件的目的地（收件人信箱）

        Log.v("lengfeng", str_smtp);
        Log.v("lengfeng", str_from_mail);
        Log.v("lengfeng", str_to_mail);

        Properties props = System.getProperties();// Get system properties

        props.put("mail.smtp.host", host);// Setup mail server

        props.put("mail.smtp.auth", "true"); // 这样才能通过验证

        PopupAuthenticator myauth = new PopupAuthenticator(str_user, str_pass);// Get
        // session

        Session session = Session.getDefaultInstance(props, myauth);

        MimeMessage message = new MimeMessage(session); // Define message

        try {
            message.setFrom(new InternetAddress(from)); // Set the from address
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                    to));// Set the to address
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            message.setSubject(str_title);// Set the subject
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            message.setText(str_body);// Set the content
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        MimeBodyPart attachPart = new MimeBodyPart();
        // FileDataSource fds = new FileDataSource(str_file_path); // 打开要发送的文件
        // try {
        // attachPart.setDataHandler(new DataHandler(fds));
        // } catch (MessagingException e) {
        // e.printStackTrace();
        // }
        // try {
        // attachPart.setFileName(fds.getName());
        // } catch (MessagingException e) {
        // e.printStackTrace();
        // }
        //
        // MimeMultipart allMultipart = new MimeMultipart("mixed"); // 附件
        // try {
        // allMultipart.addBodyPart(attachPart);// 添加
        // } catch (MessagingException e) {
        // e.printStackTrace();
        // }
        // try {
        // message.setContent(allMultipart);
        // } catch (MessagingException e) {
        // e.printStackTrace();
        // }
        try {
            message.saveChanges();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            Transport.send(message);// 开始发送
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}class PopupAuthenticator extends Authenticator {
    private String username;
    private String password;

    public PopupAuthenticator(String username, String pwd) {
        this.username = username;
        this.password = pwd;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.username, this.password);
    }
}
