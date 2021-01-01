package com.kidongyun.bridge.api.controller;

import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import com.kidongyun.bridge.api.service.ObjectiveService;
import lombok.RequiredArgsConstructor;
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
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/objective")
@Transactional
public class ObjectiveController {
    private ObjectiveService objectiveService;
    private ObjectiveRepository objectiveRepository;

    @Autowired
    public ObjectiveController(ObjectiveService objectiveService, ObjectiveRepository objectiveRepository) {}

    @GetMapping("/{id}")
    public ResponseEntity<?> getObjective(@PathVariable("id") Long id) {
        log.info("YKD : " + id);
        Optional<Objective> objOpt = objectiveRepository.findById(id);

        log.info(objOpt.toString());

        Objective obj = objOpt.orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        log.info("YKD : " + obj.getId());
        log.info("YKD : " + obj.getParent().getId());

        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.getReasonPhrase());
    }
}
