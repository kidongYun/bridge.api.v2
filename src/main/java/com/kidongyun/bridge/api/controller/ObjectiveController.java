package com.kidongyun.bridge.api.controller;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import com.kidongyun.bridge.api.service.ObjectiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("api/v1/objective")
@RequiredArgsConstructor
public class ObjectiveController {
    private final ObjectiveService objectiveService;
    private final ObjectiveRepository objectiveRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getObjective(@PathVariable("id") Long id) {
        Optional<Objective> objOpt = objectiveRepository.findById(1L);

        try {
            log.info("YKD : " + objOpt.orElseThrow(() -> new Exception("Obj is not found")).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.getReasonPhrase());
    }
}
