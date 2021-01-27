package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.repository.cell.CellRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.BiPredicate;

@Slf4j
@Service
class CellService<T extends Cell> {
    private CellRepository<T> cellRepository;

    @Autowired
    public CellService(CellRepository<T> cellRepository) {
        this.cellRepository = cellRepository;
    }

    public Set<T> findByType(Cell.Type type) {
        if(Objects.isNull(type)) {
            return Set.of();
        }

        return cellRepository.findByType(type);
    }

    public Optional<T> findById(Long id) throws Exception {
        if(Objects.isNull(id)) {
            return Optional.empty();
        }

        return cellRepository.findById(id);
    }

    public Set<T> searchByMemberEmail(String email) {
        return Set.of();
    }

    public Set<T> findByMemberEmail(String email) {
        if(Objects.isNull(email)) {
            return Set.of();
        }

        return cellRepository.findByMemberEmail(email);
    }

    public Set<T> findByMemberEmailOrderByStartDateTime(String email) {
        if(Objects.isNull(email)) {
            return Set.of();
        }

        return cellRepository.findByMemberEmailOrderByStartDateTime(email);
    }

    public Set<T> findByMemberEmailOrderByEndDateTime(String email) {
        if(Objects.isNull(email)) {
            return Set.of();
        }

        return cellRepository.findByMemberEmailOrderByEndDateTime(email);
    }

    public Optional<T> save(T cell) {
        if(Objects.isNull(cell)) {
            return Optional.empty();
        }

        return Optional.of(cellRepository.save(cell));
    }

    public void deleteById(Long id) {
        Assert.notNull(id, "'id' parameter must not be null");

        cellRepository.deleteById(id);
    }

    public List<T> order(List<T> srcList, BiPredicate<T, T> criteria) {
        List<T> ordered = new ArrayList<>();

        for(int i$=0; i$<srcList.size(); i$++) {
            T target = srcList.get(i$);
            log.info("YKD : target - " + target.toString());

            for(int j$=i$; j$<srcList.size(); j$++) {
                if(criteria.test(target, srcList.get(j$))) {
                    log.info("YKD : j$ - " + j$);
                    target = srcList.get(j$);
                    log.info("YKD : changed target - " + target);
                }
            }

            ordered.add(target);
            log.info("YKD : ordered - " + ordered.toString());
        }

        return ordered;
    }
}