package com.meta.sports.user.adapter.out.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meta.sports.general.domain.Apuesta;
import com.meta.sports.general.entitys.ApuestasEntity;
import com.meta.sports.global.Constants;
import com.meta.sports.global.utils.StringUtils;
import com.meta.sports.user.adapter.out.persistence.mapper.AddressMapper;
import com.meta.sports.user.adapter.out.persistence.mapper.UserMapper;
import com.meta.sports.user.application.port.out.FindUserPort;
import com.meta.sports.catalog.domain.Catalog;
import com.meta.sports.user.domain.Address;
import com.meta.sports.user.domain.SearchParams;
import com.meta.sports.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Log4j2
@AllArgsConstructor
public class FindUserAdapter implements FindUserPort {

    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;
    private final ObjectMapper objectMapper;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;


    @Override
    public Address address(Long user) {

        AddressEntity entity = addressRepository.findByUserId(user);

        return entity == null ? null : addressMapper.mapToDomainEntity(entity, 0);
    }

    @Override
    public User byEmail(String email) {
        return find(null, email.trim());
    }

    @Override
    public User byId(Long id) {
        return find(id, null);
    }

    @Override
    public List<User> all() {
        List<UserEntity> usersEntity= this.userRepository.findAll();

        List<User> usuarios = new ArrayList<>(usersEntity.size());


        for (UserEntity userEntity : usersEntity) {
            usuarios.add(userMapper.mapToDomainEntity(userEntity, 0));

        }

        return usuarios;

    }


    private User find(Long id, String email) {

        UserEntity entity = null;

        if (id != null) {

            Optional<UserEntity> opEntity = userRepository.findById(id);

            if (opEntity.isPresent()) entity = opEntity.get();
        }

        else if (email != null) {
            entity = userRepository.findByEmailContainingIgnoreCase(email);
        }

        if (entity != null) {
            User user = userMapper.mapToDomainEntity(entity,  1);

            Address address = address(user.getId());

            if (address != null) {
                user.setAddress(address);
            }

            return user;
        }
        else return null;
    }




}
