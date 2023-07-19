package com.meta.sports.user.adapter.out.persistence;

import com.meta.sports.global.domain.ResourceDetail;
import com.meta.sports.user.application.port.out.ProfileUserPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class ProfileUserAdapter implements ProfileUserPort {

    @Override
    public ResourceDetail findPicture(String picture) {
        return null;
    }

    @Override
    public void uploadPicture(MultipartFile picture, String filename) {

    }
}
