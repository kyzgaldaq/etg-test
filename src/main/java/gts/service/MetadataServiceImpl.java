package gts.service;

import gts.db.TableDefDaoLayer;
import gts.dtos.PaginationDTO;
import gts.dtos.TableDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MetadataServiceImpl implements MetadataService {
    TableDefDaoLayer daoLayer;
    @Override
    public TableDto createTable(TableDto dto) {
        return daoLayer.createTable(dto);
    }

    @Override
    public TableDto getTable(String tableName) {
        return daoLayer.getTableByName(tableName);
    }

    @Override
    public PaginationDTO<TableDto> getAllTables(int page, int size) {
        return daoLayer.getAllTablesPaginated(page, size);
    }
}
