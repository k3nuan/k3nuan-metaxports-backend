package com.meta.sports.events.adapter.out.persistence;

import com.meta.sports.user.adapter.out.persistence.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "event", uniqueConstraints = @UniqueConstraint(name = "userEvent", columnNames = { "type", "user_id", "created" }))
@IdClass(EventPK.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity implements Serializable {

    @Column(nullable = false)
    private String source;

    @Column
    private String origin;

    @Id
    @OneToOne
    @JoinColumn(name = "type")
    private EventTypeEntity type;

    @Id
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column
    private String detail;

    @Column(columnDefinition = "json")
    private String data;

    @Column
    private Integer errorCode;

    @Column
    private String message;

    @OneToOne
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @Id
    private LocalDateTime created;

}
