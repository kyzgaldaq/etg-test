package gts.dtos.mapper;

import gts.dtos.ColumnDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnMapper implements RowMapper<ColumnDTO>{
    @Override
    public ColumnDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ColumnDTO.builder()
                .name(rs.getString("column_name"))
                .type(rs.getString("column_type"))
                .postgresType(rs.getString("postgres_column_type"))
                .isNullable(rs.getBoolean("is_nullable"))
                .isPrimaryKey(rs.getBoolean("is_primary_key_internal"))
                .build();
    }
}
