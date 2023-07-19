package com.meta.sports.email.adapter.in.web;

import com.meta.sports.email.Port.in.OtpIn;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.user.application.port.in.FindUser;
import com.meta.sports.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class otpController {

    private final FindUser findUser;

    private final OtpIn otpIn;

    @PostMapping("/api/v1/email/{email}/validateOtp/{otp}")
    public boolean validateOtp(@PathVariable String email,@PathVariable String otp) {

       User user= this.findUser.byEmail(email);

        if (user == null) {
            throw new BadRequestException(101);
        }
        return this.otpIn.validateOtp(email, otp);

    }
}
