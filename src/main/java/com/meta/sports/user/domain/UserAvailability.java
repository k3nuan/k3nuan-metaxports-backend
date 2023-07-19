package com.meta.sports.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAvailability {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private boolean available;


    public UserAvailability(boolean available) {
        this.available = available;
    }
}