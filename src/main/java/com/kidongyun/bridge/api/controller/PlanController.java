package com.kidongyun.bridge.api.controller;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Plan;
import com.kidongyun.bridge.api.repository.plan.PlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@RestController
@RequestMapping("api/v1/plan")
@Transactional
public class PlanController {
    private PlanRepository planRepository;

    @Autowired
    public PlanController(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @GetMapping
    public ResponseEntity<?> getPlan() {
        /* PLAN 목록을 가져온다 */
        Set<Plan> plans = planRepository.findByType(Cell.Type.Plan);

        return ResponseEntity.status(HttpStatus.OK)
                .body(plans.stream().map(Plan.Response::of).collect(toSet()));
    }

    @PostMapping
    public ResponseEntity<?> postPlan(@RequestBody Plan.Post post) {
        /* */
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.getReasonPhrase());
    }
}
