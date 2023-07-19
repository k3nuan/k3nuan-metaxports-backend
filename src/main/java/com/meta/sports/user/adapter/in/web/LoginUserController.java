package com.meta.sports.user.adapter.in.web;

import com.meta.sports.global.Constants;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.user.application.port.in.FindUser;
import com.meta.sports.user.application.port.in.UpdateUser;
import com.meta.sports.user.domain.User;
import com.meta.sports.user.domain.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class LoginUserController {

    private final FindUser findUser;
    private final UpdateUser updateUser;


    @PostMapping("/api/v1/users/login")
    @PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "') or hasRole('" + Constants.ROLE_USER + "') or hasRole('"+Constants.ROLE_ALIADO +"')")
    public User login(Authentication authentication, HttpServletRequest request) {

        User user = findUser.byEmail(((UserSession) authentication.getPrincipal()).getUsername());

        if (user.getStatus().equalsIgnoreCase(User.STATUS_INACTIVE)) {
            throw new BadRequestException(155);
        } else if (user.getStatus().equalsIgnoreCase(User.STATUS_LOCKED)) {
            throw new BadRequestException(151);
        }

        updateUser.lastLogin(user.getId(), ((UserSession) authentication.getPrincipal()).getOrigin());

        return user;
    }

    @DeleteMapping("/api/v1/users/logout")
    @PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "') or hasRole('" + Constants.ROLE_USER + "')")
    public void logout(Authentication authentication, HttpServletRequest request) {
        request.getSession().invalidate();
    }

}
