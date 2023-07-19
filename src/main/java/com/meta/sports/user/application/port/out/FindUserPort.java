package com.meta.sports.user.application.port.out;

import com.meta.sports.catalog.domain.Catalog;
import com.meta.sports.user.domain.Address;
import com.meta.sports.user.domain.SearchParams;
import com.meta.sports.user.domain.User;

import java.util.List;

public interface FindUserPort {

    Address address(Long user);

    User byEmail(String email);

    User byId(Long id);

    List<User> all();


}
