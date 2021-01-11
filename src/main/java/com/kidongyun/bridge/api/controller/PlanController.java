package com.kidongyun.bridge.api.controller;

import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Plan;
import com.kidongyun.bridge.api.service.MemberService;
import com.kidongyun.bridge.api.service.ObjectiveService;
import com.kidongyun.bridge.api.service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@RestController
@RequestMapping("api/v1/plan")
@Transactional
public class PlanController {
    private PlanService planService;
    private ObjectiveService objectiveService;
    private MemberService memberService;

    @Autowired
    public PlanController(PlanService planService, ObjectiveService objectiveService, MemberService memberService) {
        this.planService = planService;
        this.objectiveService = objectiveService;
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<?> getPlan() {
        /* PLAN 목록을 가져온다 */
        Set<Plan> plans = planService.findByType(Cell.Type.Plan);

        return ResponseEntity.status(HttpStatus.OK)
                .body(plans.stream().map(Plan.Response::of).collect(toSet()));
    }

    @PostMapping
    public ResponseEntity<?> postPlan(@RequestBody Plan.Post post) throws Exception {
        Member member = memberService.findByEmail(post.getEmail());

        Objective parent = objectiveService.findById(post.getObjectiveId());

        Plan plan = planService.save(post.toDomain(member, parent));

        return ResponseEntity.status(HttpStatus.OK).body(Plan.Response.of(plan));
    }

    @PutMapping
    public ResponseEntity<?> putPlan(@RequestBody Plan.Put put) throws Exception {
        Member member = memberService.findByEmail(put.getEmail());

        Objective parent = objectiveService.findById(put.getObjectiveId());

        Plan plan = planService.save(put.toDomain(member, parent));

        return ResponseEntity.status(HttpStatus.OK).body(Plan.Response.of(plan));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable("id") Long id) throws Exception {
        planService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.getReasonPhrase());
    }
}
