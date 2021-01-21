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
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@CrossOrigin
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
        Member member = memberService.findByEmail(post.getEmail())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "'" + post.getEmail() + "' 에 해당하는 'Member' 객체를 가져오지 못했습니다"));

        Objective objective = objectiveService.findById(post.getObjectiveId())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "'" + post.getObjectiveId() + "' 에 해당하는 'Objective' 객체를 가져오지 못했습니다"));

        Plan plan = planService.save(Plan.of(post, member, objective))
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스에 새로운 'Plan' 객체를 저장하는 데에 문제가 발생했습니다"));

        return ResponseEntity.status(HttpStatus.OK).body(Plan.Response.of(plan));
    }

    @PutMapping
    public ResponseEntity<?> putPlan(@RequestBody Plan.Put put) throws Exception {
        Member member = memberService.findByEmail(put.getEmail())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "'" + put.getEmail() + "' 에 해당하는 'Member' 객체를 가져오지 못했습니다"));

        Objective objective = objectiveService.findById(put.getObjectiveId())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "'" + put.getObjectiveId() + "' 에 해당하는 'Objective' 객체를 가져오지 못했습니다"));

        Plan plan = planService.save(Plan.of(put, member, objective))
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스에 수정된 'Plan' 객체를 적용하는 데에 문제가 발생했습니다"));

        return ResponseEntity.status(HttpStatus.OK).body(Plan.Response.of(plan));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable("id") Long id) throws Exception {
        planService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.getReasonPhrase());
    }
}
