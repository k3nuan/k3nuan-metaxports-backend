package com.meta.sports.general.domain.utils;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonParser;

import javax.sql.rowset.serial.SerialBlob;


public class BlobDeserializer extends JsonDeserializer<Blob> {


    @Override
    public Blob deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        try {
            String base64 = p.getValueAsString();
            byte[] bytes = Base64.getDecoder().decode(base64);
            return new SerialBlob(bytes);
        } catch (SQLException e) {
            throw new RuntimeException("Error al deserializar Blob", e);
        }
    }
}
