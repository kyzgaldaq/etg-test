package gts.controller;

import gts.dtos.TableColumnsModel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1/dynamic-tables/data/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DataController {
    final TableMetaDataService tableMetaDataService;

    @PostMapping("{tableName}")
    public ResponseEntity<TableColumnsModel> insertData(@RequestParam("tableName") String tableName) {
        TableColumnsModel result = tableMetaDataService.insertDataByTableName(tableName);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("{tableName}/{id}")
    public ResponseEntity<TableColumnsModel> getDataByTableNameAndId(@PathVariable("tableName") String tableName, @PathVariable("id") Long id) {
        TableColumnsModel result = tableMetaDataService.getDataByTableNameAndId(tableName, id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("{tableName}/{id}")
    public ResponseEntity<TableColumnsModel> updateData(@PathVariable("tableName") String tableName, @PathVariable("id") Long id) {
        TableColumnsModel result = tableMetaDataService.updateDataByTableNameAndId(tableName, id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{tableName}/{id}")
    public ResponseEntity<Void> deleteDataByTableNameAndId(@PathVariable("tableName") String tableName, @PathVariable("id") Long id) {
        tableMetaDataService.deleteDataByTableNameAndId(tableName, id);
        return ResponseEntity.noContent().build();
    }
}
