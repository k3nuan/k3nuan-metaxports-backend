package com.meta.sports.events.adapter.out.persistence;

import com.meta.sports.user.adapter.out.persistence.UserEntity;
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
public class EventPK implements Serializable {

    private EventTypeEntity type;
    private UserEntity user;
    private LocalDateTime created;

}