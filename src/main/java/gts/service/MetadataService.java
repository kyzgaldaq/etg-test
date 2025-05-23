package gts.service;

import gts.dtos.PaginationDTO;
import gts.dtos.TableDTO;

public interface MetadataService {
    TableDTO createTable(TableDTO dto);

    TableDTO getTable(String tableName);

    PaginationDTO<TableDTO> getAllTables(int page, int size);
}
