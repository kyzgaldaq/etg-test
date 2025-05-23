package gts.service;

import gts.dtos.PaginationDTO;
import gts.dtos.TableDto;

import java.util.List;

public interface MetadataService {
    TableDto createTable(TableDto dto);

    TableDto getTable(String tableName);

    PaginationDTO<TableDto> getAllTables(int page, int size);
}
