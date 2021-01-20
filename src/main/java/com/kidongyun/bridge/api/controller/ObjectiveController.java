package com.kidongyun.bridge.api.controller;

import com.kidongyun.bridge.api.aspect.ExecuteLog;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.service.MemberService;
import com.kidongyun.bridge.api.service.ObjectiveService;
import com.kidongyun.bridge.api.service.PriorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;

import java.util.Objects;
import java.util.Set;

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
    public ResponseEntity<?> getObjective() {
        /* OBJECTIVE 목록을 가져온다 */
        Set<Objective> objectives = objectiveService.findByType(Cell.Type.Objective);

        return ResponseEntity.status(HttpStatus.OK)
                .body(objectives.stream().map(Objective.Response::of).collect(toSet()));
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
    public ResponseEntity<?> getObjectiveByEmail(@PathVariable("email") String email) throws Exception {
        Set<Objective.Response> response =
                objectiveService.findByMemberEmail(email).stream().map(Objective.Response::of).collect(toSet());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExecuteLog
    @PostMapping
    public ResponseEntity<?> postObjective(@RequestBody Objective.Post post) throws Exception {
        /* PRIORITY 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Priority priority = priorityService.findByIdAndMemberEmail(post.getPriorityId(), post.getEmail());

        /* MEMBER 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Member member = memberService.findByEmail(post.getEmail());

        /* PARENT 로 연결한 OBJECTIVE 를 가져온다. 없다면 없는 상태로 Objective 생성 */
        Objective parent = Objective.builder().build();
        if(Objects.isNull(post.getParentId())) {
            parent = objectiveService.findById(post.getParentId()).orElse(Objective.builder().build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(Objective.Response.of(objectiveService.save(Objective.of(post, priority, member, parent))));
    }

    @ExecuteLog
    @PutMapping
    public ResponseEntity<?> putObjective(@RequestBody Objective.Put put) throws Exception {
        /* PRIORITY 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Priority priority = priorityService.findByIdAndMemberEmail(put.getPriorityId(), put.getEmail());

        /* MEMBER 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Member member = memberService.findByEmail(put.getEmail());

        /* PARENT 로 연결한 OBJECTIVE 를 가져온다. 없다면 없는 상태로 Objective 생성 */
        Objective parent = objectiveService.findById(put.getParentId());

        return ResponseEntity.status(HttpStatus.OK).body(Objective.Response.of(objectiveService.save(Objective.of(put, priority, member, parent))));
    }

    @ExecuteLog
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteObjective(@PathVariable("id") Long id) {
        objectiveService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.getReasonPhrase());
    }
}
