package com.meta.sports.user.application.service;

import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.global.exceptions.NotFoundException;
import com.meta.sports.user.application.port.in.AuthUser;
import com.meta.sports.user.application.port.out.AuthUserPort;
import com.meta.sports.user.application.port.out.FindUserPort;
import com.meta.sports.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthUserService implements AuthUser {

    @Value("${app.users.password.regex}")
    private String passwordRegex;

    @Autowired
    private AuthUserPort authUserPort;

    @Autowired
    private FindUserPort findUserPort;


    @Override
    public User authenticate(String username, String password, String origin) {

        User user = authUserPort.withEmail(username, password);

        if(user == null) {
            throw new BadCredentialsException("Bad Credentials");
        }
        user = findUserPort.byEmail(user.getEmail());
        user.setPassword(password);

        return user;
    }

    @Override
    public void changePassword(String email, String password) {

        if (email == null || email.trim().length() < 5) {
            throw new NotFoundException();
        }

        User user = new User(email);
        user.setPassword(password);

        authUserPort.changePassword(user);
    }

    @Override
    public void changePassword(User user) {
        if (user.getPassword() == null || user.getPasswordConfirm() == null
            || user.getPassword().trim().length() < 8 || user.getPasswordConfirm().trim().length() < 8
            || user.getPassword().trim().length() > 72 || user.getPasswordConfirm().trim().length() > 72
            || user.getPassword().trim().compareTo(user.getPasswordConfirm().trim()) != 0
            || !user.getPassword().matches(passwordRegex)) {
            throw new BadRequestException(101);
        }

        List<String> dataSensitive = new ArrayList<>();
        dataSensitive.add(user.getEmail().split("@")[0]);
        dataSensitive.add(user.getFirstName().toLowerCase());
        dataSensitive.add(user.getLastName().toLowerCase());

        String domainSensitive = user.getEmail().split("@")[1];
        domainSensitive = domainSensitive.substring(0, domainSensitive.lastIndexOf("."));

        dataSensitive.add(domainSensitive);

        if (dataSensitive.stream().anyMatch(dt -> user.getPassword().indexOf(dt) >= 0)) {
            throw new BadRequestException(101);
        }

        authUserPort.changePassword(user);
    }

    @Override
    public boolean emailAvailability(String email) {
        try {
            return findUserPort.byEmail(email) == null && findUserPort.byEmail(email) == null;
        } catch (NotFoundException e) {
            return true;
        }
    }
}