package com.kidongyun.bridge.api.controller;

import com.kidongyun.bridge.api.aspect.ExecuteLog;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;

import static java.util.stream.Collectors.toSet;

@Slf4j
@RestController
@Transactional
@RequestMapping("api/v1/objective")
public class ObjectiveController {
    private ObjectiveRepository objectiveRepository;

    @Autowired
    public ObjectiveController(ObjectiveRepository objectiveRepository) {
        this.objectiveRepository = objectiveRepository;
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

    @PostMapping
    public ResponseEntity<?> postObjective(@RequestBody Objective.Post param) {
        log.info("YKD : " + param.toString());
        log.info("YKD : " + param.toDomain().toString());

        Objective obj = objectiveRepository.save(param.toDomain());

        log.info("YKD : " + obj.toString());

        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }
}
