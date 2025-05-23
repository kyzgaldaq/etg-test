package gts.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColumnDTO {
    @NotNull
    @Size(min = 3, max = 63)
    @Pattern(regexp = "^(?!pg_)(?!app_)[a-z0-9_]+$")
    String name;
    @NotNull
    String type;
    Boolean isNullable;
    String postgresType;
    Boolean isPrimaryKey;

}
