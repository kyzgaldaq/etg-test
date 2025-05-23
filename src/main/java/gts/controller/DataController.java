package gts.controller;

import gts.dtos.PaginationDTO;
import gts.dtos.TableColumnsModel;
import gts.service.TableMetaDataService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController("/api/v1/dynamic-tables/data/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataController {
     final TableMetaDataService tableMetaDataService;

    @PostMapping("{tableName}")
    public ResponseEntity<Map<String, Object>> insertData(@RequestParam("tableName") String tableName, @RequestBody Map<String, Object> data) {
        Map<String, Object> result = tableMetaDataService.insertDataByTableName(tableName, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("{tableName}/{id}")
    public ResponseEntity<Map<String, Object>> getDataByTableNameAndId(@PathVariable("tableName") String tableName, @PathVariable("id") Long id) {
        Map<String, Object> result = tableMetaDataService.getDataByTableNameAndId(tableName, id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("{tableName}/{id}")
    public ResponseEntity<Map<String, Object>> updateData(@PathVariable("tableName") String tableName, @PathVariable("id") Long id) {
        Map<String, Object> result = tableMetaDataService.updateDataByTableNameAndId(tableName, id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{tableName}/{id}")
    public ResponseEntity<Void> deleteDataByTableNameAndId(@PathVariable("tableName") String tableName, @PathVariable("id") Long id) {
        tableMetaDataService.deleteDataByTableNameAndId(tableName, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{tableName}")
    public PaginationDTO<Map<String, Object>> getDataPaginated(@PathVariable("tableName") String tableName,
                                                             @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                             @RequestParam(value = "size", defaultValue = "5") Integer size) {
       return tableMetaDataService.getDataPaginated(tableName, page, size);
    }
}
