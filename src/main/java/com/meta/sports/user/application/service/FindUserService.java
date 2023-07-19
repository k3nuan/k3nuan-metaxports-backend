package com.meta.sports.user.application.service;

import com.meta.sports.catalog.domain.Catalog;
import com.meta.sports.user.adapter.out.persistence.UserRepository;
import com.meta.sports.user.application.port.in.FindUser;
import com.meta.sports.user.application.port.out.FindUserPort;
import com.meta.sports.user.domain.Address;
import com.meta.sports.user.domain.SearchParams;
import com.meta.sports.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindUserService implements FindUser {

    @Autowired
    
    private FindUserPort findUserPort;

     @Autowired
    UserRepository userRepository;

    @Override
    public Address address(Long user) {
        return findUserPort.address(user);
    }

    @Override
    public User byEmail(String email) {
        return findUserPort.byEmail(email);
    }

    @Override
    public User byId(Long id) {

        return id == null || id.longValue() < 1 ? null : findUserPort.byId(id);
    }

    @Override
    public List<User> all() {
     return   findUserPort.all();
    }


}
