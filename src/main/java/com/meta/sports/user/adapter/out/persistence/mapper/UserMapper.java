package com.meta.sports.user.adapter.out.persistence.mapper;

import com.meta.sports.global.Constants;
import com.meta.sports.user.adapter.out.persistence.RoleRepository;
import com.meta.sports.user.adapter.out.persistence.UserEntity;
import com.meta.sports.user.domain.Address;
import com.meta.sports.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private RoleRepository roleRepository;

    public UserEntity mapToJpaEntity(User user, Address address) {
       /* return new UserEntity(
                user.getId(),
                user.getPassword(),
                null,
                roleRepository.findByName(user.getRole().replaceAll(Constants.ROLE_PREFIX, "")),
                user.getStatus(),
                user.getFirstName(),
                user.getLastName(),
                user.getSaldo(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthDate(),
                null,
                user.getGender(),
                user.getProfilePic(),
                null,
                null,
                user.getLastLogin(), address == null ? null : addressMapper.mapToJpaEntity(user.getId(), address),
                user.getCreated());*/

        return null;
    }

    public User mapToDomainEntity(UserEntity entity, int securityLevel) {

        User user = new User(entity.getId(), entity.getEmail())
                .withRole(entity.getRole().getName())
                .withStatus(entity.getStatus());

        user.setLastLogin(entity.getLastLogin());

        user.setEmail(entity.getEmail());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setSaldo(entity.getSaldo());
        user.setName(user.getFirstName() + " " + user.getLastName());
        user.setPhone(entity.getPhone());
        user.setBirthDate(entity.getBirthDate());
        user.setGender(entity.getGender());
        user.setProfilePic(entity.getImgProfile());
        user.setCreated(entity.getCreated());
        user.setCodigoReferido(entity.getCodigo_referido());

        switch (securityLevel) {
            case 1:
                user.setUsername(user.getEmail());
                return user;

            default:
                return user;
        }
    }

}
