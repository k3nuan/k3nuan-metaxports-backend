package com.meta.sports.user.application.service;

import com.meta.sports.user.application.port.out.ProfileUserPort;
import com.meta.sports.global.domain.ResourceDetail;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.global.exceptions.NotFoundException;
import com.meta.sports.user.application.port.in.ProfileUser;
import com.meta.sports.user.application.port.out.FindUserPort;
import com.meta.sports.user.application.port.out.UpdateUserPort;
import com.meta.sports.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileUserService implements ProfileUser {

    @Autowired
    private FindUserPort findUserPort;

    @Autowired
    private ProfileUserPort profileUserPort;

    @Autowired
    private UpdateUserPort updateUserPort;

    @Override
    public ResourceDetail findPicture(String picture) {
        return profileUserPort.findPicture(picture);
    }

    @Override
    public User uploadPicture(Long user, MultipartFile picture) {

        User userData = null;

        try {
            userData = findUserPort.byId(user);
        } catch (NotFoundException e) {
            throw new BadRequestException(101);
        }

        String filename = Sha512DigestUtils.shaHex(String.valueOf(user) + userData.getUsername() + "profile");

        profileUserPort.uploadPicture(picture, filename);
        updateUserPort.profilePicture(user, filename, user);

        userData.setProfilePic(filename);

        return userData;
    }

}
