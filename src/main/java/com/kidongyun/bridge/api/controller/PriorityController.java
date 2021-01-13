package com.kidongyun.bridge.api.controller;

import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.service.PriorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("api/v1/priority")
@Transactional
public class PriorityController {
    private PriorityService priorityService;

    @Autowired
    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @GetMapping
    public ResponseEntity<?> getPriority() {
        return ResponseEntity.status(HttpStatus.OK).body(priorityService.findAll().stream().map(Priority.Response::of).collect(toList()));
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getPriorityByEmail(@PathVariable("email") String email) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(priorityService.findByMemberEmail(email).stream().map(Priority.Response::of).collect(toSet()));
    }

    @PostMapping
    public ResponseEntity<?> postPriority() {
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.getReasonPhrase());
    }
}
