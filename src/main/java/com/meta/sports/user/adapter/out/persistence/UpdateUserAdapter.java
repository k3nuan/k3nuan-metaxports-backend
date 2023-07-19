package com.meta.sports.user.adapter.out.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meta.sports.user.adapter.out.persistence.mapper.AddressMapper;
import com.meta.sports.user.application.port.out.UpdateUserPort;
import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.user.domain.SearchParams;
import com.meta.sports.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class UpdateUserAdapter implements UpdateUserPort {

    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public void lastLogin(Long id, String origin) {
        origin = "0:0:0:0:0:0:0:1";
        userRepository.saveLastLoginById(origin, id);
    }


    @Override
    @Transactional
    public void profileData(User profile, Long modifiedBy) {

        userRepository.saveBasicDataById(profile.getFirstName(), profile.getLastName(),
            profile.getGender(), profile.getPhone(), modifiedBy, profile.getId());

        if (profile.getAddress() != null) {
            addressRepository.saveAndFlush(addressMapper.mapToJpaEntity(profile.getId(), profile.getAddress()));
        }
    }


    @Override
    @Transactional
    public void profilePicture(Long id, String picture, Long modifiedBy) {
        userRepository.saveProfilePicById(picture, modifiedBy, id);
    }


    @Override
    @Transactional
    public void status(Long id, String status, Long modifiedBy) {
        userRepository.saveStatusById(status, modifiedBy, id);
    }

}
