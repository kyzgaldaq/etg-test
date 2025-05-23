package gts.service;

import gts.dtos.PaginationDTO;

import java.util.Map;

public interface TableMetaDataService {
    Map<String, Object> insertDataByTableName(String tableName, Map<String, Object> data);

    Map<String, Object> getDataByTableNameAndId(String tableName, Long id);

    Map<String, Object> updateDataByTableNameAndId(String tableName, Long id, Map<String, Object> data);

    void deleteDataByTableNameAndId(String tableName, Long id);

    PaginationDTO<Map<String, Object>> getDataPaginated(String tableName, int page, int size);
}
