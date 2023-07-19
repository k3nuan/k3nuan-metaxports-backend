package com.meta.sports.global.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;

@Getter
@Setter
public class ResourceDetail {

    private String contentType;

    private ByteArrayResource data;

}
