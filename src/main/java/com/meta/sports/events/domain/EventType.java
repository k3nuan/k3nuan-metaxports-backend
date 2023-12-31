package com.meta.sports.events.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventType implements Serializable {

    private Integer id;
    private String name;
    private String description;

    public EventType(Integer id) { this.id = id; }

}