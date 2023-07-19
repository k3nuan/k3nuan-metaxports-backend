package com.meta.sports.user.adapter.out.persistence;

import com.meta.sports.global.exceptions.BadRequestException;
import com.meta.sports.user.adapter.out.persistence.mapper.UserMapper;
import com.meta.sports.user.application.port.out.AuthUserPort;
import com.meta.sports.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthUserAdapter implements AuthUserPort {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void changePassword(User user) {
        UserEntity userEntity= userRepository.findByEmailContainingIgnoreCase(user.getUsername());
        userEntity.setPassword(Sha512DigestUtils.shaHex(user.getPassword()));
        userRepository.saveAndFlush(userEntity);
    }

    @Override
    public User withEmail(String email, String password) {
        UserEntity userEntity = userRepository.findByEmailAndPassword(email, Sha512DigestUtils.shaHex(password));

        if(userEntity == null){
            return null;
        }

        User user = userMapper.mapToDomainEntity(userEntity, 1);

        return user;
    }
}
