package hutech.mixture.petstore.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender mailSender;
    public void sendSetPasswordEmail(String email, String resetPasswordToken) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Quên mật khẩu");
        mimeMessageHelper.setText(
                String.format(
                        "<html>" +
                                "<body>" +
                                "<p>Vui lòng nhấn vào nút bên dưới để đặt mật khẩu mới:</p>" +
                                "<a href='http://localhost:8080/auth/reset-password?verificationCode=%s' style='display: inline-block; padding: 10px 20px; background-color: blue; color: white; text-decoration: none; border-radius: 4px;'>Nhấn vào đây</a>" +
                                "</body>" +
                                "</html>",
                        resetPasswordToken
                ),
                true
        );
        mailSender.send(mimeMessage);
    }
}
