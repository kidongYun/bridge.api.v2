package com.kidongyun.bridge.api.controller;

import com.kidongyun.bridge.api.aspect.ExecuteLog;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.repository.cell.CellRepository;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.stream.Collectors.toSet;

@Slf4j
@RestController
@RequestMapping("api/v1/objective")
public class ObjectiveController {
    private ObjectiveRepository objectiveRepository;
    private CellRepository cellRepository;

    @Autowired
    public ObjectiveController(ObjectiveRepository objectiveRepository, CellRepository cellRepository) {
        this.objectiveRepository = objectiveRepository;
        this.cellRepository = cellRepository;
    }

    @GetMapping
    public ResponseEntity<?> getObjective() {
        return ResponseEntity.status(HttpStatus.OK).body(objectiveRepository.findByType(Cell.Type.Objective)
                .stream().map(Objective.Response::of).collect(toSet()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getObjectiveById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(Objective.Response.of(objectiveRepository.findByIdAndType(id, Cell.Type.Objective)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR))));
    }

    @ExecuteLog
    @PostMapping
    public ResponseEntity<?> postObjective(@RequestBody Objective.Post param) {
        Objective obj = objectiveRepository.save(Objective.builder().startDateTime(LocalDateTime.now()).endDateTime(LocalDateTime.now())
                .status("completed").type(Cell.Type.Objective).title("title1").description("desc1").build());

//        if(Objects.isNull(obj)) {
//            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
//        }

        log.info("YKD : " +  cellRepository.findAll().size());

        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }
}
