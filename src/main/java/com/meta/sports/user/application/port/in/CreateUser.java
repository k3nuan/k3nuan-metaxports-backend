package com.meta.sports.user.application.port.in;

import com.meta.sports.catalog.domain.Catalog;
import com.meta.sports.user.domain.Address;
import com.meta.sports.user.domain.User;

public interface CreateUser {

    void address(Address address, Long user);

    User byEmail(User user);

    User byReferenceOf(User user, User userReference);

    User createAliado (User user);

}
