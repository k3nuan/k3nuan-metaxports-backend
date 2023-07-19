package com.meta.sports.user.application.port.out;

import com.meta.sports.catalog.domain.Catalog;
import com.meta.sports.user.domain.User;

public interface CreateUserPort {

    void address(String street, String suite, String city, String state, Integer country, Integer zipCode, Long user);

    User byEmail(User user);

    User createAliado(User user);

}
