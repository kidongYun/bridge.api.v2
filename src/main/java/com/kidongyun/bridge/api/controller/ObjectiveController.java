package com.kidongyun.bridge.api.controller;

import com.kidongyun.bridge.api.aspect.ExecuteLog;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.service.MemberService;
import com.kidongyun.bridge.api.service.ObjectiveService;
import com.kidongyun.bridge.api.service.PriorityService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@CrossOrigin
@RestController
@Transactional
@RequestMapping("api/v1/objective")
public class ObjectiveController {
    private ObjectiveService objectiveService;
    private PriorityService priorityService;
    private MemberService memberService;

    @Autowired
    public ObjectiveController(ObjectiveService objectiveService, PriorityService priorityService, MemberService memberService) {
        this.objectiveService = objectiveService;
        this.priorityService = priorityService;
        this.memberService = memberService;
    }

    @ExecuteLog
    @GetMapping
    public ResponseEntity<?> getObjective(Objective.Get get) throws Exception {
        /* PRIORITY 정보를 가져온다. */
        Priority priority = priorityService.findByIdAndMemberEmail(get.getPriorityId(), get.getEmail()).orElse(null);

        /* MEMBER 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Member member = memberService.findByEmail(get.getEmail()).orElse(null);

        /* 부모 OBJECTIVE 를 가져온다. 없다면 빈 Objective 생성 */
        Objective parent = objectiveService.findById(get.getParentId()).orElse(null);

        Objective filter = Objective.of(get, priority, member, parent);

        List<Objective.Response> responses = objectiveService.findByObjective(filter)
                .stream().map(Objective.Response::of).collect(toList());

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @ExecuteLog
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getObjectiveById(@PathVariable("id") Long id) throws Exception {
        if(Objects.isNull(id)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "parameter 'id' must not be null");
        }

        Objective objective = objectiveService.findById(id).orElseThrow(
                () -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "'id' 에 해당하는 'objective' 객체를 찾을 수 없습니다"));

        return ResponseEntity.status(HttpStatus.OK).body(Objective.Response.of(objective));
    }

    @ExecuteLog
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getObjectiveByEmail(@ApiParam(example = "john@gmail.com") @PathVariable("email") String email) throws Exception {
        Set<Objective.Response> response =
                objectiveService.findByMemberEmail(email).stream().map(Objective.Response::of).collect(toSet());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExecuteLog
    @PostMapping
    public ResponseEntity<?> postObjective(@RequestBody Objective.Post post) throws Exception {
        /* PRIORITY 정보를 가져온다. */
        Priority priority = priorityService.findByIdAndMemberEmail(post.getPriorityId(), post.getEmail())
                .orElse(priorityService.findAnyOne().orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "'Priority' 정보가 존재하지 않습니다")));

        /* MEMBER 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Member member = memberService.findByEmail(post.getEmail())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "'" + post.getEmail() + "' 에 해당하는 'Member' 객체를 가져오지 못했습니다"));

        /* 부모 OBJECTIVE 를 가져온다. 없다면 null 생성 */
        Objective parent = objectiveService.findById(post.getParentId()).orElse(null);

        /* 새로운 OBJECTIVE 객체 데이터베이스에 저장 */
        Objective result = objectiveService.save(Objective.of(post, priority, member, parent))
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스에 새로운 Objective 객체를 저장하는 데에 문제가 발생했습니다"));

        return ResponseEntity.status(HttpStatus.OK).body(Objective.Response.of(result));
    }

    @ExecuteLog
    @PutMapping("/{id}")
    public ResponseEntity<?> putObjective(@PathVariable("id") Long id, @RequestBody Objective.Put put) throws Exception {
        /* PRIORITY 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Priority priority = priorityService.findByIdAndMemberEmail(put.getPriorityId(), put.getEmail())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "'" + put.getPriorityId() + "' 에 해당하는 'Priority' 객체를 가져오지 못했습니다"));

        /* MEMBER 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Member member = memberService.findByEmail(put.getEmail())
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "'" + put.getEmail() + "' 에 해당하는 'Member' 객체를 가져오지 못했습니다"));

        /* 부모 OBJECTIVE 를 가져온다. 없다면 null 생성 */
        Objective parent = objectiveService.findById(put.getParentId()).orElse(null);

        /* 수정 된 OBJECTIVE 객체 데이터베이스에 업데이트 */
        Objective result = objectiveService.save(Objective.of(put, priority, member, parent))
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스에 수정된 Objective 객체를 적용하는 데에 문제가 발생했습니다"));

        return ResponseEntity.status(HttpStatus.OK).body(Objective.Response.of(result));
    }

    @ExecuteLog
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteObjective(@PathVariable("id") Long id) {
        objectiveService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.getReasonPhrase());
    }
}
