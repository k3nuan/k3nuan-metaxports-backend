package com.meta.sports.email.adapter.out;
import  com.meta.sports.email.Port.out.EmailOut;
import com.meta.sports.email.domain.EmailValueDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
@AllArgsConstructor
public class EmailAdapter implements EmailOut {


    private  final JavaMailSender javaMailSender;


    private  final TemplateEngine templateEngine;

    @Override
    public void sendEmailTemplate(EmailValueDTO dto) {

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("username", dto.getUsername());
            model.put("codigo",dto.getTokenPassword());
            context.setVariables(model);
            String htmlText = templateEngine.process("Email", context);
            helper.setFrom(dto.getEmailFrom());
            helper.setTo(dto.getEmailTo());
            helper.setSubject(dto.getSubject());
            helper.setText(htmlText, true);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }
}
