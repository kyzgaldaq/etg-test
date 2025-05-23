package gts.db;

import gts.ColumnMapper;
import gts.TableMapper;
import gts.dtos.ColumnDto;
import gts.dtos.PaginationDTO;
import gts.dtos.TableDto;
import gts.exceptions.CustomAlreadyExistsException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableDefDaoLayer {
   final JdbcClient jdbcClient;

   public TableDto createTable(TableDto dto){
      if(checkTableExists(dto.getTableName()))
         throw new CustomAlreadyExistsException(String.format("Table %s already exists", dto.getTableName()));

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
                                         true
                                         )
                                 .update();
                  }
          );

          return dto;
   }

   public TableDto getTableByName(String tableName) {
      String tableSql = "SELECT * FROM app_dynamic_table_definitions WHERE table_name = ?";
      String columnsSql = "SELECT * FROM app_dynamic_column_definitions WHERE table_definition_id = ?";

         TableDto response = jdbcClient.sql(tableSql)
                 .param(1, tableName)
                 .query(new TableMapper())
                 .single();

         int tableId = getTableId(tableName);

         List<ColumnDto> columns = jdbcClient.sql(columnsSql)
                 .param(1, tableId)
                 .query(new ColumnMapper()).list();

         response.setColumns(columns);
         return response;

   }

   public PaginationDTO<TableDto> getAllTablesPaginated(int page, int size) {
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

      List<TableDto> tables = jdbcClient.sql(sql)
              .params(size,offset)
              .query((rs, rowNum) ->
                      TableDto.builder()
                      .tableName(rs.getString("table_name"))
                      .userFriendlyName(rs.getString("user_friendly_name"))
                      .columnCount(rs.getInt("column_count"))
                      .build())
              .list();

      return new PaginationDTO<>(tables, page, size, totalElements);
   }

   public boolean checkTableExists(String tableName) {
      String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = ? AND table_name = ?";

      int count = jdbcClient.sql(sql)
              .params("public", tableName)
              .query(Integer.class)
              .single();

      return count > 0;
   }

   public List<ColumnDto> getColumnsByTableName(String tableName) {

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
         default -> null;
      };
   }

   private int getTableId(String tableName) {
      String sql = "select id from information_schema.tables where table_name = ?";
      return jdbcClient.sql(sql).param(1,tableName).query(Integer.class).single();
   }

}
