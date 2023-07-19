package com.meta.sports.email.service;

import com.meta.sports.email.Port.in.OtpIn;
import com.meta.sports.email.Port.out.OtpOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OtpInService implements OtpIn {

    @Autowired
    OtpOut otp;

    @Override
    public boolean validateOtp(String email,String otp) {
        return this.otp.validateOtp(email,otp);
    }
}
