package com.meta.sports.user.application.port.in;

import com.meta.sports.global.domain.ResourceDetail;
import com.meta.sports.user.domain.User;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileUser {

    ResourceDetail findPicture(String picture);

    User uploadPicture(Long user, MultipartFile picture);

}
