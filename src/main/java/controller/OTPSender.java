package controller;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class OTPSender {

    private static final String username = "naradamahendra@gmail.com";
    private static final String password = "nklu imay hnlh slkq";

    private static final String numbers = "0123456789";
    private static SecureRandom random = new SecureRandom();

    public static String generateOTP(int length) {
        StringBuilder otp = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            otp.append(numbers.charAt(random.nextInt(numbers.length())));
        }
        String generatedOtp = otp.toString();
        OtpStorage.storeOtp(generatedOtp);
        return otp.toString();
    }



    public static void sendOTP(String recipientEmail, String otp) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Gmail SMTP host
        properties.put("mail.smtp.port", "587"); // Gmail SMTP port // SMTP port

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your OTP Code");
            message.setText("Your OTP code is: " + otp);

            Transport.send(message);
            System.out.println("OTP sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendOrderConfirmationEmail(String recipientEmail, String customerName, String orderId, List<String> orderedItems, double totalAmount,String cashier) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password); // Replace with your email username and password
            }
        });

        try {
            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // Replace with your email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Order Confirmation: Order #" + orderId);


            StringBuilder emailBody = new StringBuilder();
            emailBody.append("Dear ").append(customerName).append(",\n\n");
            emailBody.append("Thank you for your purchase! Here are the details of your order:\n\n");
            emailBody.append("Order ID: ").append(orderId).append("\n");
            emailBody.append("Cashier : ").append(cashier).append("\n");
            emailBody.append("Ordered Items:\n");

            for (String item : orderedItems) {
                emailBody.append("- ").append(item).append("\n");
            }

            emailBody.append("\nTotal Amount: $").append(String.format("%.2f", totalAmount)).append("\n");
            emailBody.append("\nWe hope you enjoy your purchase. If you have any questions, feel free to contact us.\n\n");
            emailBody.append("Best regards,\nCloth Store Team");

            // Set the email body
            message.setText(emailBody.toString());

            // Send the email
            Transport.send(message);
            System.out.println("Order confirmation sent successfully to " + recipientEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
