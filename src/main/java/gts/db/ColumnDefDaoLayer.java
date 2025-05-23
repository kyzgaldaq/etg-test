package gts.db;

import gts.dtos.ColumnDTO;
import gts.dtos.PaginationDTO;
import gts.exceptions.CustomIllegalArgumentException;
import gts.exceptions.CustomNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

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

       List<ColumnDTO> columns = daoLayer.getTableByName(tableName).getColumns();
        Map<String, ColumnDTO> columnMap = columns.stream()
                .collect(Collectors.toMap(ColumnDTO::getName, c -> c));

        for (String key : data.keySet()) {
            if (!columnMap.containsKey(key)) {
                throw new CustomIllegalArgumentException(String.format("Column %s does not exist", key));
            }
        }

        for (ColumnDTO col : columns) {
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
        if (!daoLayer.checkTableExists(tableName)) {
            throw new CustomNotFoundException(String.format("Table %s does not exist", tableName));
        }
        String sql = "select * from " + tableName + " where id = ?";
        String colSql= "select c.column_name from app_dynamic_table_definitions a join app_dynamic_column_definitions c on a.id = c.table_definition_id where a.table_name = ?";
        List<String> columnNames = jdbcClient.sql(colSql).param(tableName).query(String.class).list();
        return jdbcClient.sql(sql).param(id).query((rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            for (String name : columnNames) {
                row.put(name, rs.getObject(name));
            }
            return row;
        }).single();
    }

    public void deleteDataByTableNameAndId(String tableName, Long id) {
        if (!daoLayer.checkTableExists(tableName)) {
            throw new CustomNotFoundException(String.format("Table %s does not exist", tableName));
        }
        String sql = "delete from " + tableName + " where id = ?";
        jdbcClient.sql(sql).params(id).update();
    }

    public Map<String, Object> updateDataByTableNameAndId(String tableName, Long id, Map<String, Object> data) {
        String colSql= "select c.column_name from app_dynamic_table_definitions a join app_dynamic_column_definitions c on a.id = c.table_definition_id where a.table_name = ?";
        List<String> columnNames = jdbcClient.sql(colSql).param(tableName).query(String.class).list();
        StringBuilder sb = new StringBuilder();
        List<Object> rec = new ArrayList<>();
        sb.append("update ").append(tableName).append(" set ");
        for (String name : columnNames) {
            if(name.equals("id")){
                continue;
            }
            sb.append(name).append(" = ?, ");
            rec.add(data.get(name));
        }
       sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("where id = ?");
        rec.add(id);
        jdbcClient.sql(sb.toString()).params(rec).update();
        return getDataByTableNameAndId(tableName, id);
    }

    public PaginationDTO<Map<String, Object>> getDataPaginated(String tableName, int page, int size) {
        int offset = page * size;

        int totalElements = jdbcClient
                .sql("select count(*) from " + tableName)
                .query(Integer.class)
                .single();

        String sql = "select * from "+ tableName + " order by id limit ? offset ? ";
        String colSql= "select c.column_name from app_dynamic_table_definitions a join app_dynamic_column_definitions c on a.id = c.table_definition_id where a.table_name = ?";
        List<String> columnNames = jdbcClient.sql(colSql).param(tableName).query(String.class).list();

        List<Map<String, Object>> res = jdbcClient.sql(sql)
                .params(size, offset)
                .query((rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            for (String name : columnNames) {
                row.put(name, rs.getObject(name));
            }
            return row;
        }).list();

        return new PaginationDTO<>(res, page, size, totalElements);
    }
}
