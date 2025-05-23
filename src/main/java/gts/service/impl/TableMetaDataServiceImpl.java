package gts.service.impl;

import gts.db.ColumnDefDaoLayer;
import gts.dtos.PaginationDTO;
import gts.service.TableMetaDataService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TableMetaDataServiceImpl implements TableMetaDataService {
    ColumnDefDaoLayer daoLayer;

    @Transactional
    @Override
    public Map<String, Object> insertDataByTableName(String tableName, Map<String, Object> data) {
        return daoLayer.insertDataByTableName(tableName, data);
    }

    @Override
    public Map<String, Object> getDataByTableNameAndId(String tableName, Long id) {
        return daoLayer.getDataByTableNameAndId(tableName, id);
    }

    @Override
    public Map<String, Object> updateDataByTableNameAndId(String tableName, Long id, Map<String, Object> data) {
        return daoLayer.updateDataByTableNameAndId(tableName, id, data);
    }

    @Override
    public void deleteDataByTableNameAndId(String tableName, Long id) {
        daoLayer.deleteDataByTableNameAndId(tableName, id);
    }

    @Override
    public PaginationDTO<Map<String, Object>> getDataPaginated(String tableName, int page, int size) {
        return daoLayer.getDataPaginated(tableName, page, size);
    }
}
