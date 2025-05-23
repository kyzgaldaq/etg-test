package gts.controller;

import gts.dtos.PaginationDTO;
import gts.service.MetadataService;
import gts.dtos.TableDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dynamic-tables/schemas/")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableDefinitionController {
   final MetadataService metadataService;

   @PostMapping
   public ResponseEntity<TableDTO> createTable(@RequestBody TableDTO dto) {
      TableDTO created = metadataService.createTable(dto);
      return ResponseEntity.status(HttpStatus.CREATED).body(created);
   }

   @GetMapping("/{tableName}")
   public ResponseEntity<TableDTO> getTable(@PathVariable String tableName) {
      TableDTO result = metadataService.getTable(tableName);
      return ResponseEntity.ok(result);
   }

   @GetMapping
   public PaginationDTO<TableDTO> getAllTables(
                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "size", defaultValue = "5") Integer size) {
      return metadataService.getAllTables(page, size);
   }
}
