package com.meta.sports.user.application.port.in;

import com.meta.sports.user.domain.SearchParams;
import com.meta.sports.user.domain.User;

import java.time.LocalDateTime;

public interface UpdateUser {

    void lastLogin(Long id, String origin);

    void profileData(User profile, Long modifiedBy);

    void status(Long id, String status, User modifiedBy);
}
