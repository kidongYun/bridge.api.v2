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

import javax.transaction.Transactional;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
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

    @GetMapping
    public ResponseEntity<?> getObjective() {
        /* OBJECTIVE 목록을 가져온다 */
        Set<Objective> objectives = objectiveService.findByType(Cell.Type.Objective);

        return ResponseEntity.status(HttpStatus.OK)
                .body(objectives.stream().map(Objective.Response::of).collect(toSet()));
    }

    @ExecuteLog
    @GetMapping("/{id}")
    public ResponseEntity<?> getObjectiveById(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(Objective.Response.of(objectiveService.findById(id)));
    }

    @ExecuteLog
    @PostMapping
    public ResponseEntity<?> postObjective(@RequestBody Objective.Post post) throws Exception {
        /* PRIORITY 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Priority priority = priorityService.findByIdAndMemberEmail(post.getPriorityId(), post.getEmail());

        /* MEMBER 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Member member = memberService.findByEmail(post.getEmail());

        /* PARENT 로 연결한 OBJECTIVE 를 가져온다. 없다면 없는 상태로 Objective 생성 */
        Objective parent = objectiveService.findById(post.getParentId());

        return ResponseEntity.status(HttpStatus.OK).body(Objective.Response.of(objectiveService.save(post.toDomain(priority, member, parent))));
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

        return ResponseEntity.status(HttpStatus.OK).body(Objective.Response.of(objectiveService.save(put.toDomain(priority, member, parent))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteObjective(@PathVariable("id") Long id) {
        objectiveService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.getReasonPhrase());
    }
}
