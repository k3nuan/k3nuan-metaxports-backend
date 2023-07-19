package com.meta.sports.user.adapter.in.web;

import com.meta.sports.global.Constants;
import com.meta.sports.user.application.port.in.FindUser;
import com.meta.sports.user.application.port.in.ProfileUser;
import com.meta.sports.user.application.port.in.UpdateUser;
import com.meta.sports.global.domain.ResourceDetail;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.global.exceptions.NotFoundException;
import com.meta.sports.user.domain.User;
import com.meta.sports.user.domain.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users/profile")
@RequiredArgsConstructor
public class ProfileUserController {

    @Value("${app.files.profile.pic.max-size}")
    private Integer profilePicMaxsize;

    private final FindUser findUser;
    private final ProfileUser profileUser;
    private final UpdateUser updateUser;


    @GetMapping("/img")
    @PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "') or hasRole('" + Constants.ROLE_USER + "')")
    public ResponseEntity<ByteArrayResource> findProfilePicture(Authentication authentication) {

        User user = findUser.byId(((UserSession) authentication.getPrincipal()).getId());

        if (user == null) {
            throw new BadRequestException(101);
        }

        if (user.getProfilePic() == null) {
            return ResponseEntity.noContent().build();
        }

        try {
            ResourceDetail resource = profileUser.findPicture(user.getProfilePic());

            if (resource == null) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(resource.getContentType()))
                    .header(HttpHeaders.CONTENT_LENGTH, "" + resource.getData().contentLength())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=profilePic." + resource.getContentType().split("/")[1])
                    .body(resource.getData());
        } catch (NotFoundException nte) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/data")
    @PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "') or hasRole('" + Constants.ROLE_USER + "')")
    public void updateProfileData(Authentication authentication, @RequestBody User profile) {

        profile.setId(((UserSession) authentication.getPrincipal()).getId());

        updateUser.profileData(profile, ((UserSession) authentication.getPrincipal()).getId());
    }

    @PutMapping("/img")
    @PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "') or hasRole('" + Constants.ROLE_USER + "')")
    public User uploadProfilePicture(Authentication authentication, @RequestPart MultipartFile pic) {

        String filetype = pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf(".") + 1);

        switch (filetype.toLowerCase()) {
            case "jpg":
            case "jpeg":
            case "png":
                break;
            default:
                throw new NotFoundException();
        }

        if (pic.getSize() / 1024 > profilePicMaxsize) {
            throw new BadRequestException(301, profilePicMaxsize);
        }

        return profileUser.uploadPicture(((UserSession) authentication.getPrincipal()).getId(), pic);
    }
}
