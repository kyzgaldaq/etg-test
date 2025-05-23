package gts.db;

import gts.dtos.ColumnDto;
import gts.dtos.TableDto;
import gts.exceptions.CustomIllegalArgumentException;
import gts.exceptions.CustomNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.ResultSetMetaData;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColumnDefDaoLayer {
    final JdbcClient jdbcClient;
    final TableDefDaoLayer daoLayer;
    public Map<String, Object> insertDataByTableName(String tableName, Map<String, Object> data) {
        List<String> columnNames = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        if (!daoLayer.checkTableExists(tableName)) {
            throw new CustomNotFoundException(String.format("Table %s does not exist", tableName));
        }

       List<ColumnDto> columns = daoLayer.getTableByName(tableName).getColumns();
        Map<String, ColumnDto> columnMap = columns.stream()
                .collect(Collectors.toMap(ColumnDto::getName, c -> c));

        for (String key : data.keySet()) {
            if (!columnMap.containsKey(key)) {
                throw new CustomIllegalArgumentException(String.format("Column %s does not exist", key));
            }
        }

        for (ColumnDto col : columns) {
            String name = col.getName();
            if (!"id".equalsIgnoreCase(name) && data.containsKey(name)) {
                columnNames.add(name);
                values.add(data.get(name));
            }
        }

        String columnSql = String.join(", ", columnNames);
        String placeholders = columnNames.stream().map(c -> "?").collect(Collectors.joining(", "));
        jdbcClient.sql("insert into " + tableName + " (" + columnSql + ") VALUES (" + placeholders + ")")
                .params(values.toArray())
                .update();

        return new HashMap<>(data);
    }

    public Map<String, Object> getDataByTableNameAndId(String tableName, Long id) {
        Map<String, Object> data = new HashMap<>();
        if (!daoLayer.checkTableExists(tableName)) {
            throw new CustomNotFoundException(String.format("Table %s does not exist", tableName));
        }
        String sql = "select * from " + tableName + " where id = ?";
        jdbcClient.sql(sql).params(id).query((rs, rowNum) -> {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                Object value = rs.getObject(i);
                data.put(columnName, value);
            }

            return null;

        });
        return data;
    }

    public void deleteDataByTableNameAndId(String tableName, Long id) {
        if (!daoLayer.checkTableExists(tableName)) {
            throw new CustomNotFoundException(String.format("Table %s does not exist", tableName));
        }
        String sql = "delete from " + tableName + " where id = ?";
        jdbcClient.sql(sql).params(id).update();
    }
}
