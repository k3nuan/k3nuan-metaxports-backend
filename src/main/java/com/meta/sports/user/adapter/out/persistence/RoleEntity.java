package com.meta.sports.user.adapter.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

}