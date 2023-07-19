package com.meta.sports.user.adapter.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserIdCreatedPK implements Serializable {

    private Long userId;
    private LocalDateTime created;

}
