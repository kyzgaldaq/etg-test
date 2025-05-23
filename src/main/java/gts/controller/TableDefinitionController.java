package gts.controller;

import gts.dtos.PaginationDTO;
import gts.service.MetadataService;
import gts.dtos.TableDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/v1/dynamic-tables/schemas/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableDefinitionController {
   final MetadataService metadataService;

   @PostMapping
   public ResponseEntity<TableDto> createTable(@RequestBody TableDto dto) {
      TableDto created = metadataService.createTable(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
   }

   @GetMapping("/{tableName}")
   public ResponseEntity<TableDto> getTable(@PathVariable String tableName) {
      TableDto result = metadataService.getTable(tableName);
      return ResponseEntity.ok(result);
   }

   @GetMapping
   public PaginationDTO<TableDto> getAllTables(
                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "size", defaultValue = "5") Integer size) {
      return metadataService.getAllTables(page, size);
   }
}
