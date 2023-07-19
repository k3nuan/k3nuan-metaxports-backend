package com.meta.sports.user.application.port.in;


import com.meta.sports.user.domain.User;

public interface AuthUser {

    User authenticate(String username, String password, String origin);

    void changePassword(String email, String password);

    void changePassword(User user);

    boolean emailAvailability(String email);

}
