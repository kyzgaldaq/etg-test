package gts;

import gts.dtos.TableDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TableMapper  implements RowMapper<TableDto> {
    @Override
    public TableDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TableDto.builder()
                .tableName(rs.getString("table_name"))
                .userFriendlyName(rs.getString("user_friendly_name"))
                .build();
    }
}
