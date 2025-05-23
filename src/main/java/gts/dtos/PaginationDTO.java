package gts.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaginationDTO<T> {

     List<T> content;
     Map<String, Object> mapContent;

     int page;

     int size;

     int totalPages;

     long totalElements;

    public PaginationDTO(List<T> content, int page, int size, long totalElements) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.totalElements = totalElements;
    }

    public PaginationDTO(Map<String, Object> content, int page, int size, long totalElements) {
        this.mapContent = content;
        this.page = page;
        this.size = size;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
        this.totalElements = totalElements;
    }


}
