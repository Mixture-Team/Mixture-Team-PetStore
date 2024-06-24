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
                                "<p>Dưới đây là mã xác thực để đặt lại mật khẩu:</p>" +
                                "<p><strong>%s</strong></p>" +
                                "</body>" +
                                "</html>",
                        resetPasswordToken
                ),
                true
        );
        mailSender.send(mimeMessage);
    }
}
