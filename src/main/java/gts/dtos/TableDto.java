package gts.dtos;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableDto {
    String tableName;
    String userFriendlyName;
    int columnCount;
    List<ColumnDto> columns;
}
