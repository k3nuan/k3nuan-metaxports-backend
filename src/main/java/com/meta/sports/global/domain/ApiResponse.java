package com.meta.sports.global.domain;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class ApiResponse {

    private String uuid;

    private LocalDateTime dateTime;

    private Integer status;

    private Integer code;

    private String message;

    private String error;

    private DataResponse data;

    private Map<String, String> errors;


    public ApiResponse() {}

    public ApiResponse(Integer status, Integer code, String message) {
        this.uuid = UUID.randomUUID().toString();
        this.dateTime = LocalDateTime.now();
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public ApiResponse withErrors(Map<String, String> errors) {
        this.errors = errors;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public DataResponse getData() {
        return data;
    }

    public void setData(DataResponse data) {
        this.data = data;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}