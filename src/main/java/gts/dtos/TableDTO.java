package gts.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableDTO {
    @NotNull
    @Size(min = 3, max = 63)
    @Pattern(regexp = "^(?!pg_)(?!app_)[a-z0-9_]+$")
    String tableName;
    String userFriendlyName;
    int columnCount;
    List<ColumnDTO> columns;
}
