package com.meta.sports.user.application.port.out;

import com.meta.sports.global.domain.ResourceDetail;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileUserPort {

    ResourceDetail findPicture(String picture);

    void uploadPicture(MultipartFile picture, String filename);

}
