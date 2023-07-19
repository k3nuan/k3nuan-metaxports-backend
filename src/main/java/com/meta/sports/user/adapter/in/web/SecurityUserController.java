package com.meta.sports.user.adapter.in.web;

import com.meta.sports.catalog.application.port.in.FindCatalog;
import com.meta.sports.global.Constants;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.user.application.port.in.AuthUser;
import com.meta.sports.user.application.port.in.CreateUser;
import com.meta.sports.user.application.port.in.FindUser;
import com.meta.sports.user.application.port.in.UpdateUser;
import com.meta.sports.user.domain.User;
import com.meta.sports.user.domain.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SecurityUserController {

    @Value("${app.users.email.regex}")
    private String emailRegex;

    private final AuthUser authUser;
    private final FindCatalog findCatalog;
    private final CreateUser createUser;
    private final FindUser findUser;
    private final UpdateUser updateUser;

    private final HttpServletRequest request;


    @PutMapping("/api/v1/users/{email}/security/password")

    public void changePassword(@PathVariable String email, @RequestParam("psw") String password) {
        authUser.changePassword(email, password);
    }

    @GetMapping("/api/v1/users/emails/{email}/available")
    public boolean getEmailAvailability(@PathVariable String email) {

        if (!email.matches(emailRegex)) {
            throw new BadRequestException(101);
        }

        return authUser.emailAvailability(email.toLowerCase());
    }

    @GetMapping("/api/v1/users/{email}/user")
    public User findUserByEmail(@PathVariable String email) {

        if (!email.matches(emailRegex)) {
            throw new BadRequestException(101);
        }

        return findUser.byEmail(email);
    }

    @GetMapping("/api/v1/users/allUsers")
    public List<User> findAllUsers() {

        return findUser.all();
    }

    @PostMapping("/api/v1/users/password")
    public void changePassword(Authentication authentication, @RequestBody User userData) {

        User user = findUser.byId(((UserSession) authentication.getPrincipal()).getId());

        if (user == null) {
            throw new BadRequestException(102);
        }
        else if (!user.hasRole(Constants.ROLE_ADMIN, true)) {
            if (user.getStatus().equalsIgnoreCase(User.STATUS_INACTIVE)) {
                throw new BadRequestException(155);
            } else if (user.getStatus().equalsIgnoreCase(User.STATUS_LOCKED)) {
                throw new BadRequestException(151);
            }
        }

        if (!user.hasRole(Constants.ROLE_ADMIN, true)) {

            if (userData.getOldPassword() == null || userData.getOldPassword().trim().length() < 5) {
                throw new BadRequestException(101);
            }

            try {
                authUser.authenticate(user.getEmail(), userData.getOldPassword(), "");
            } catch(org.springframework.security.authentication.BadCredentialsException e) {
                throw new BadRequestException(103, e.getMessage());
            }
        }

        user.setUsername(((UserSession) authentication.getPrincipal()).getUsername());
        user.setPassword(userData.getPassword());
        user.setPasswordConfirm(userData.getPasswordConfirm());

        authUser.changePassword(user);

        //TODO eval sign-out action to access with new password
        //request.getSession().invalidate();
    }

    @PostMapping("/api/v1/user/join")
    public User joinUser(@RequestBody User user) {
        user.setStatus(User.STATUS_ACTIVE);
        user.setRole(Constants.ROLE_USER.replaceAll(Constants.ROLE_PREFIX, ""));
        user.setPassword(Sha512DigestUtils.shaHex(user.getPassword()));
        return createUser.byEmail(user);
    }
    @PostMapping("/api/v1/user/joinAdmin")

    public User joinAdmin(@RequestBody User user) {
        user.setStatus(User.STATUS_ACTIVE);
        user.setRole(Constants.ROLE_ADMIN.replaceAll(Constants.ROLE_PREFIX,""));
        user.setPassword(Sha512DigestUtils.shaHex(user.getPassword()));
        return createUser.byEmail(user);
    }
    @PostMapping("/api/v1/user/joinAliado")

    public User joinAliado(@RequestBody User user) {
        user.setStatus(User.STATUS_ACTIVE);
        user.setRole(Constants.ROLE_ALIADO.replaceAll(Constants.ROLE_PREFIX,""));
        user.setPassword(Sha512DigestUtils.shaHex(user.getPassword()));
        return createUser.byEmail(user);
    }






}
