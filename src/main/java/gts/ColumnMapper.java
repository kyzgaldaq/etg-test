package gts;

import gts.dtos.ColumnDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnMapper implements RowMapper<ColumnDto>{
    @Override
    public ColumnDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ColumnDto.builder()
                .name(rs.getString("column_name"))
                .type(rs.getString("column_type"))
                .postgresType(rs.getString("postgres_column_type"))
                .isNullable(rs.getBoolean("is_nullable"))
                .isPrimaryKey(rs.getBoolean("is_primary_key"))
                .build();
    }
}
