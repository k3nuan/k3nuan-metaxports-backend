package com.meta.sports.user.application.port.out;

import com.meta.sports.user.domain.User;

public interface AuthUserPort {

    void changePassword(User user);

    User withEmail(String email, String password);

}
