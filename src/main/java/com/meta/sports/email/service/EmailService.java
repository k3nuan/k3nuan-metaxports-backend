package com.meta.sports.email.service;

import com.meta.sports.email.Port.in.EmailIN;
import com.meta.sports.email.Port.out.EmailOut;
import com.meta.sports.email.Port.out.OtpOut;
import com.meta.sports.email.Port.out.OtpOut;
import com.meta.sports.email.domain.EmailValueDTO;
import com.meta.sports.global.exceptions.NotFoundException;
import com.meta.sports.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service

public class EmailService  implements EmailIN {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    OtpOut otp;


    @Autowired
    EmailOut email;

    @Value("${mail.subject}")
    private  String subject;


    @Value("${spring.mail.username}")
    private  String emailFrom;

    public void sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("migueld2310@gmail.com");
        message.setTo("migueld2310@gmail.com");
        message.setSubject("Prueba envio");
        message.setText("contenido del email");
        javaMailSender.send(message);
    }



    @Override
    public Boolean sendEmail(EmailValueDTO dto,User usuario) {

        try {
            dto.setEmailFrom(emailFrom);
            dto.setSubject(subject);
            dto.setUsername(usuario.getUsername());
            String codigo= otp.generateOtp(6);
            dto.setTokenPassword(codigo);
            otp.crearOtp(usuario.getEmail(), codigo);
            email.sendEmailTemplate(dto);
            return true;
        } catch (NotFoundException e) {
            return true;
        }



    }
}
