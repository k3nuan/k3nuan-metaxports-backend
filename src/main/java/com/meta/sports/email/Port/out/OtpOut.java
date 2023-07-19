package com.meta.sports.email.Port.out;

import org.springframework.http.ResponseEntity;

public interface OtpOut {



       String generateOtp(int len);


       Boolean validateOtp(String email,String otp);

       ResponseEntity crearOtp (String email,String otp);

}
