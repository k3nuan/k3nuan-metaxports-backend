package com.meta.sports.global.exceptions;

import com.meta.sports.global.domain.DataResponse;

public class BadRequestException extends RuntimeException {

    private final Integer code;

    private Object[] parameters;

    private DataResponse dataResponse;

    public BadRequestException(Integer code) {
        this.code = code;
    }

    public BadRequestException(Integer code, Object... parameters) {
        this.code = code;
        this.parameters = parameters;
    }

    public BadRequestException(Integer code, DataResponse dataResponse, Object... parameters) {
        this.code = code;
        this.parameters = parameters;
        this.dataResponse = dataResponse;
    }

    public Integer getCode() {
        return code;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public DataResponse getDataResponse() {
        return dataResponse;
    }

    public void setDataResponse(DataResponse dataResponse) {
        this.dataResponse = dataResponse;
    }
}