package com.meta.sports.email.Port.in;


import com.meta.sports.email.domain.EmailValueDTO;
import com.meta.sports.user.domain.User;

public interface EmailIN {


    Boolean sendEmail(EmailValueDTO dto,User usuario);






}