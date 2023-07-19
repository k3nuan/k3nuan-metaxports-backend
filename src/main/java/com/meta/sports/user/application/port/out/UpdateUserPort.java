package com.meta.sports.user.application.port.out;

import com.meta.sports.user.domain.SearchParams;
import com.meta.sports.user.domain.User;

import java.time.LocalDateTime;

public interface UpdateUserPort {

    void lastLogin(Long id, String origin);

    void profileData(User profile, Long modifiedBy);

    void profilePicture(Long id, String picture, Long modifiedBy);

    void status(Long id, String status, Long modifiedBy);

}
