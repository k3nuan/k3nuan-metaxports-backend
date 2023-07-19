package com.meta.sports.email.Port.out;


import com.meta.sports.email.domain.EmailValueDTO;

public interface EmailOut {


    void sendEmailTemplate(EmailValueDTO dto);





}