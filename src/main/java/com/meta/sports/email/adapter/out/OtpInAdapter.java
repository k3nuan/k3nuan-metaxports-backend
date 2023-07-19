package com.meta.sports.email.adapter.out;


import com.meta.sports.email.Port.out.OtpOut;
import com.meta.sports.user.adapter.out.persistence.UserEntity;
import com.meta.sports.user.adapter.out.persistence.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import java.util.*;

@Component
@Log4j2
@AllArgsConstructor

public class OtpInAdapter implements OtpOut {


    private final UserRepository userRepository;



    private static final long OTP_VALID_DURATION = 5 * 60 * 1000;   // 5 minutes


    @Override
    public String generateOtp(int len) {
        String numbers = "0123456789";
        Random rndm_method = new Random();


        String otp ="";

        for (int i = 0; i <len; i++) {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
            otp = otp + numbers.charAt(rndm_method.nextInt(numbers.length()));
        }


        return otp;

    }

    @Override
    public Boolean validateOtp(String email,String otp) {

        UserEntity  userEntity = userRepository.findByEmailContainingIgnoreCase(email);



        if (!userEntity.getOtp().equals(otp)){


            return false;
        }



        if (userEntity.getOtpTime() == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long otpRequestedTime= userEntity.getOtpTime().getTime();

        if (otpRequestedTime + OTP_VALID_DURATION < currentTime) {
            // OTP expires
            return false;
        }

        return true;

    }

    @Override
    public ResponseEntity crearOtp(String email,String otp) {

          UserEntity  entity = userRepository.findByEmailContainingIgnoreCase(email);

          entity.setOtp(otp);
          entity.setOtpTime(new Date());
          userRepository.save(entity);

          return new ResponseEntity("Otp Creada con exito", HttpStatus.OK);

    }





}
