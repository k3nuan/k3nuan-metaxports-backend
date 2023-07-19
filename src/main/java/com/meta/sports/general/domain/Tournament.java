package com.meta.sports.general.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.meta.sports.general.domain.utils.BlobDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.net.URI;
import java.sql.Blob;
import java.sql.Date;
import java.time.LocalDateTime;


@Getter
@Setter
public class Tournament {

    private Long id;

    private String name;

    private LocalDateTime startDate;

    private LocalDateTime endDate;


    private String imagenBase64;

    private String imagen;

    public Tournament() {}

    public Tournament(Long id) {
        this.id = id;
    }


    public Tournament(Long id, String name,LocalDateTime startDate,LocalDateTime endDate,String imagen,String imagenBase64) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imagen=imagen;
        this.imagenBase64=imagenBase64;
    }




}
