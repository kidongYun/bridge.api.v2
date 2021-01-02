package com.kidongyun.bridge.api.controller;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import com.kidongyun.bridge.api.service.ObjectiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Slf4j
@RestController
@RequestMapping("api/v1/objective")
@Transactional
public class ObjectiveController {
    private ObjectiveService objectiveService;
    private ObjectiveRepository objectiveRepository;

    @Autowired
    public ObjectiveController(ObjectiveService objectiveService, ObjectiveRepository objectiveRepository) {
        this.objectiveService = objectiveService;
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
}
