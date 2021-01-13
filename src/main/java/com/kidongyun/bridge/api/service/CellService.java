package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.repository.cell.CellRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;
import java.util.Set;

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
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'type' parameter must not be null");
        }

        return cellRepository.findByType(type);
    }

    public T findById(Long id) throws Exception {
        if(Objects.isNull(id)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'id' parameter must not be null");
        }

        return cellRepository.findById(id).orElseThrow(Exception::new);
    }

    public Set<T> findByMemberEmail(String email) {
        if(Objects.isNull(email)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'email' parameter must not be null");
        }

        return cellRepository.findByMemberEmail(email);
    }

    public T save(T cell) {
        if(Objects.isNull(cell)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'cell' parameter must not be null");
        }

        return cellRepository.save(cell);
    }

    public void deleteById(Long id) {
        if(Objects.isNull(id)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'id' parameter must not be null");
        }

        cellRepository.deleteById(id);
    }
}