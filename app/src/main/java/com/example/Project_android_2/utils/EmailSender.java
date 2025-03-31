package com.example.Project_android_2.utils;

import android.util.Log;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailSender {

    public static void sendOTP(String recipientEmail, int otpCode ,String messageContent) {
        final String username = "trungtin.le1505@gmail.com"; // Thay bằng địa chỉ email của bạn
        final String password = "kfkf xzhw fdwa rdow"; // Thay bằng mật khẩu của bạn
        String subject = otpCode + " là mã xác nhận tài khoản Solo Leveling của bạn";

        String emailContent = "<div >"
                + "<br><p style='font-size: 24px; margin: 0;'><img  src='https://fpytgnoybnnnhflnybhp.supabase.co/storage/v1/object/public/media-storage//logo_app.jpg'/></p>"
                + "<p> <span style='color:rgb(0,0,0);font-size:15px'>Chào bạn <br></span></p>"
                + "<p>" +
                "<span style='color:rgb(0,0,0);font-size:15px'>"+messageContent+", Mã xác nhận là </span>" +
                "<span style='color:rgb(0,0,0)'>" +
                "<strong><span style='color:rgb(78,164,220);font-size:15px'>" + otpCode + "</span></strong>" +
                "<span style='font-size:15px'>.</span>" +
                "</span>" +
                "  </p>"
                + "<p><span style='color:rgb(0,0,0);font-size:15px'>Vui lòng hoàn thành xác nhận trong vòng 30 phút.</span></p>"
                + "<div>" +
                "<p><span style='color:rgb(0,0,0);font-size:15px'>Solo Leveling</span></p>" +
                "<h5><span style='color:rgb(119,119,119);font-size:13px'>Đây là thư từ hệ thống, vui lòng không trả lời thư.</span></h5>" +
                "</div>"
                + "</div>";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        // Gửi thư
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "Solo Leveling"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject(subject);
            message.setContent(emailContent, "text/html; charset=utf-8");

            // Gửi email
            Transport.send(message);
            Log.d("EmailSender", "Gửi email thành công"); // Ghi log khi email được gửi thành công

        } catch (MessagingException e) {
            e.printStackTrace();
            Log.e("EmailSender", "Gửi email thất bại : " + e.getMessage()); // Ghi log nếu có lỗi xảy ra khi gửi email
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
