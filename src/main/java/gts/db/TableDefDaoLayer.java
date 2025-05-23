package gts.db;

import gts.dtos.mapper.ColumnMapper;
import gts.dtos.mapper.TableMapper;
import gts.dtos.ColumnDTO;
import gts.dtos.PaginationDTO;
import gts.dtos.TableDTO;
import gts.exceptions.CustomAlreadyExistsException;
import gts.exceptions.CustomIllegalArgumentException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableDefDaoLayer {
   final JdbcClient jdbcClient;

   public TableDTO createTable(TableDTO dto){
      if(checkTableExists(dto.getTableName())) {
         throw new CustomAlreadyExistsException(String.format("Table %s already exists", dto.getTableName()));
      }
      String sqlDef = "insert into app_dynamic_table_definitions(table_name, user_friendly_name) " +
              "values (?,?)" ;

           jdbcClient.sql(sqlDef).param(1, dto.getTableName())
                   .param(2, dto.getUserFriendlyName()).update();


       String sqlColumns = "insert into app_dynamic_column_definitions(table_definition_id, column_name, column_type," +
               " postgres_column_type, is_nullable, is_primary_key_internal)" +
               "values (?,?,?,?,?,?)" ;


          dto.getColumns().forEach( x -> {
                         jdbcClient.sql(sqlColumns)
                                 .params(
                                         getTableId(dto.getTableName()),
                                         x.getName(),
                                         x.getType(),
                                         getPostgeSqlType(x.getType()),
                                         x.getIsNullable(),
                                         x.getIsPrimaryKey()
                                         )
                                 .update();
                  }
          );

        jdbcClient.sql(buildCreateTableSql(dto)).update();


          return dto;
   }

   public TableDTO getTableByName(String tableName) {
      String tableSql = "SELECT * FROM app_dynamic_table_definitions WHERE table_name = ?";
      String columnsSql = "SELECT * FROM app_dynamic_column_definitions WHERE table_definition_id = ?";

         TableDTO response = jdbcClient.sql(tableSql)
                 .params(tableName)
                 .query(new TableMapper())
                 .single();

         int tableId = getTableId(tableName);

         List<ColumnDTO> columns = jdbcClient.sql(columnsSql)
                 .params(tableId)
                 .query(new ColumnMapper()).stream().toList();

         response.setColumns(columns);
         return response;

   }

   public PaginationDTO<TableDTO> getAllTablesPaginated(int page, int size) {
      int offset = page * size;

      int totalElements = jdbcClient
              .sql("select count(*) from app_dynamic_table_definitions")
              .query(Integer.class)
              .single();

      String sql = """
        select d.id, d.table_name, d.user_friendly_name,
               (select count(*) from app_dynamic_column_definitions c where c.table_definition_id = d.id) AS column_count
        from app_dynamic_table_definitions d
        order by d.id
        limit ? offset ?
    """;

      List<TableDTO> tables = jdbcClient.sql(sql)
              .params(size,offset)
              .query((rs, rowNum) ->
                      TableDTO.builder()
                      .tableName(rs.getString("table_name"))
                      .userFriendlyName(rs.getString("user_friendly_name"))
                      .columnCount(rs.getInt("column_count"))
                      .build())
              .list();

      return new PaginationDTO<>(tables, page, size, totalElements);
   }

   public boolean checkTableExists(String tableName) {
      String sql = "SELECT to_regclass(?) IS NOT NULL";

      String fullTableName = "public." + tableName;

      return jdbcClient.sql(sql)
              .params(fullTableName)
              .query(Boolean.class)
              .single();
   }


   private String getPostgeSqlType(String type) {
      return switch (type) {
         case "String" -> "VARCHAR";
         case "Text" -> "TEXT";
         case "Integer" -> "INTEGER";
         case "Bigint" -> "BIGINT";
         case "Boolean" -> "BOOLEAN";
         case "Timestamp" -> "TIMESTAMP WITHOUT TIME ZONE ";
         case "Date" -> "DATE";
         case "Decimal" -> "NUMERIC(19, 4)";
         case "Serial" -> "SERIAL";
         default -> null;
      };
   }

   private int getTableId(String tableName) {
      String sql = "select id from app_dynamic_table_definitions where table_name = ?";
      return jdbcClient.sql(sql).param(1,tableName).query(Integer.class).single();
   }


   private String buildCreateTableSql(TableDTO dto) {
      StringBuilder sb = new StringBuilder();
      sb.append("create table if not exists ").append(dto.getTableName()).append(" (");

      List<String> columnDefs = new ArrayList<>();

      for (ColumnDTO col : dto.getColumns()) {
         StringBuilder colDef = new StringBuilder();
         colDef.append(col.getName()).append(" ").append(getPostgeSqlType(col.getType()));

         if (!col.getIsNullable()) {
            colDef.append(" NOT NULL");
         }

         if(col.getIsPrimaryKey()){
            colDef.append(" PRIMARY KEY");
         }
         columnDefs.add(colDef.toString());
      }
      sb.append(columnDefs);
      int index = sb.indexOf("[");
      sb.deleteCharAt(index);
      index = sb.indexOf("]");
      sb.deleteCharAt(index);
      sb.append(");");
      System.out.println(sb);
      return sb.toString();
   }
}
