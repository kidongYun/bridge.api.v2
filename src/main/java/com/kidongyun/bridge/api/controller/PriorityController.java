package com.kidongyun.bridge.api.controller;

import com.kidongyun.bridge.api.service.PriorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@Slf4j
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
        return ResponseEntity.status(HttpStatus.OK).body(priorityService.findAll());
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getPriorityByEmail(@PathVariable("email") String email) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(priorityService.findByMemberEmail(email));
    }
}
