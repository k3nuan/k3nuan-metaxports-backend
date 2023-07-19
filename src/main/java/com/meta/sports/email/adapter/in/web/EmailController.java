package com.meta.sports.email.adapter.in.web;

import com.meta.sports.email.domain.ChangePassword;
import com.meta.sports.email.domain.EmailValueDTO;
import com.meta.sports.email.Port.in.EmailIN;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.user.application.port.in.FindUser;
import com.meta.sports.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class EmailController {


    private final FindUser findUser;

    private final EmailIN email;


    
    @PostMapping("/api/v1/email-password/email-send")
    public boolean sendEmailTemplate(@RequestBody EmailValueDTO dto) {

        User usuarioOp= findUser.byEmail(dto.getEmailTo());

        if (usuarioOp.getEmail().isEmpty()){
            throw new BadRequestException(102);
        }
        return email.sendEmail(dto,usuarioOp);

    }




}
