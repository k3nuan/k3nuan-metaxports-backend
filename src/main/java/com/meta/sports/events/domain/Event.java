package com.meta.sports.events.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.meta.sports.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event implements Serializable {

    private String source;
    private String origin;
    private EventType type;
    private User user;
    private String detail;

    @JsonRawValue
    private String data;

    @JsonIgnore
    private Map<String, Object> dataMap = new HashMap<>();

    private Integer errorCode = 0;
    private String message;
    private User createdBy;
    private LocalDateTime created;

    @JsonRawValue
    private String finalResource;

    @JsonIgnore
    private Object finalResourceObject;

    @JsonRawValue
    private String initResource;

    @JsonIgnore
    private Object initResourceObject;

    public Event(String source, String origin, Integer type, String name, User user, Integer errorCode, String message) {
        this.source = source;
        this.origin = origin;
        this.type = new EventType(type, name, null);
        this.user = user;
        this.errorCode = errorCode;
        this.message = message;
    }

    public Event createdBy(User user) { this.createdBy = user; return this; }

    public Event withDetail(String detail) { this.detail = detail; return this; }

    public void addDataMapElement(String key, Object value) {
        this.dataMap = this.dataMap == null ? new HashMap<>() : this.dataMap;
        this.dataMap.put(key, value);
    }

    public void setType(Integer type) { this.type = new EventType(type, "", null); }
    public void setType(Integer type, String name) { this.type = new EventType(type, name, null); }
}