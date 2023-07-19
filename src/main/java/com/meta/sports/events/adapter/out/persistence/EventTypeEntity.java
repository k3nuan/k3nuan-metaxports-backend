package com.meta.sports.events.adapter.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "event_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventTypeEntity implements Serializable {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

}