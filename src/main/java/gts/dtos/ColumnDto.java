package gts.dtos;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColumnDto {
    String name;
    String type;
    Boolean isNullable;
    String postgresType;
    Boolean isPrimaryKey;
}
