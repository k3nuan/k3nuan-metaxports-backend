package com.meta.sports.user.application.service;

import com.meta.sports.catalog.domain.Catalog;
import com.meta.sports.user.application.port.in.CreateUser;
import com.meta.sports.user.application.port.out.CreateUserPort;
import com.meta.sports.user.application.port.out.FindUserPort;
import com.meta.sports.user.domain.Address;
import com.meta.sports.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService implements CreateUser {

    @Autowired
    private CreateUserPort createUserPort;

    @Autowired
    private FindUserPort findUserPort;

    @Override
    public void address(Address address, Long user) {

        findUserPort.byId(user); //throw NotFoundException

        if (findUserPort.address(user) == null) {
            createUserPort.address(address.getStreet(), address.getSuite(), address.getCity(), address.getState().getKey(),
                address.getCountry().getId().intValue(), address.getZipCode(), user);
        }
        else {
            //TODO implement update address
        }
    }

    @Override
    public User byEmail(User user) {
        return createUserPort.byEmail(user);
    }

    @Override
    public User byReferenceOf(User user, User userReference) {

        //TODO eval write of userReference
        user.setUsername("REFofUSER=" + userReference.getId());

        return createUserPort.byEmail(user);
    }

    @Override
    public User createAliado(User user) {
        return createUserPort.createAliado(user);
    }
}
