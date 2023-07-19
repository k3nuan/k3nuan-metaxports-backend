package com.meta.sports.global.mapper;

import com.meta.sports.catalog.domain.Catalog;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CatalogRowMapper implements RowMapper<Catalog> {

    @Override
    public Catalog mapRow(ResultSet rs, int i) throws SQLException {

        Catalog clubInfo = new Catalog();

        clubInfo.setId(rs.getLong("id"));
        clubInfo.setValue(rs.getString("item"));

        return clubInfo;
    }
}
