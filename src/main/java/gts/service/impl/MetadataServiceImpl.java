package gts.service.impl;

import gts.db.TableDefDaoLayer;
import gts.dtos.PaginationDTO;
import gts.dtos.TableDTO;
import gts.service.MetadataService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MetadataServiceImpl implements MetadataService {
   final TableDefDaoLayer daoLayer;

    @Transactional
    @Override
    public TableDTO createTable(TableDTO dto) {
        return daoLayer.createTable(dto);
    }

    @Override
    public TableDTO getTable(String tableName) {
        return daoLayer.getTableByName(tableName);
    }

    @Override
    public PaginationDTO<TableDTO> getAllTables(int page, int size) {
        return daoLayer.getAllTablesPaginated(page, size);
    }
}
