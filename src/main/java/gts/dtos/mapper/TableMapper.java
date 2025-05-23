package gts.dtos.mapper;

import gts.dtos.TableDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TableMapper  implements RowMapper<TableDTO> {
    @Override
    public TableDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TableDTO.builder()
                .tableName(rs.getString("table_name"))
                .userFriendlyName(rs.getString("user_friendly_name"))
                .build();
    }
}
