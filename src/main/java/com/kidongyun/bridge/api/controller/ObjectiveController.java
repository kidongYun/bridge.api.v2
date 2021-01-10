package com.kidongyun.bridge.api.controller;

import com.kidongyun.bridge.api.aspect.ExecuteLog;
import com.kidongyun.bridge.api.entity.Cell;
import com.kidongyun.bridge.api.entity.Member;
import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.repository.member.MemberRepository;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import com.kidongyun.bridge.api.repository.priority.PriorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Slf4j
@RestController
@Transactional
@RequestMapping("api/v1/objective")
public class ObjectiveController {
    private ObjectiveRepository objectiveRepository;
    private PriorityRepository priorityRepository;
    private MemberRepository memberRepository;

    @Autowired
    public ObjectiveController(ObjectiveRepository objectiveRepository, PriorityRepository priorityRepository, MemberRepository memberRepository) {
        this.objectiveRepository = objectiveRepository;
        this.priorityRepository = priorityRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public ResponseEntity<?> getObjective() {
        /* OBJECTIVE 목록을 가져온다 */
        Set<Objective> objectives = objectiveRepository.findByType(Cell.Type.Objective);

        return ResponseEntity.status(HttpStatus.OK)
                .body(objectives.stream().map(Objective.Response::of).collect(toSet()));
    }

    @ExecuteLog
    @GetMapping("/{id}")
    public ResponseEntity<?> getObjectiveById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(Objective.Response.of(objectiveRepository.findByIdAndType(id, Cell.Type.Objective)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "'id' parameter is not appropriate."))));
    }

    @ExecuteLog
    @PostMapping
    public ResponseEntity<?> postObjective(@RequestBody Objective.Post post) {
        /* PRIORITY 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Priority priority = priorityRepository.findByIdAndMemberEmail(post.getPriorityId(), post.getEmail())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'priorityId', 'email' parameters are not appropriate."));

        /* MEMBER 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Member member = memberRepository.findByEmail(post.getEmail())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'email' parameter is not appropriate."));

        /* PARENT 로 연결한 OBJECTIVE 를 가져온다. 없다면 없는 상태로 Objective 생성 */
        Objective parent = objectiveRepository.findById(post.getParentId())
                .orElse(null);

        return ResponseEntity.status(HttpStatus.OK).body(Objective.Response.of(objectiveRepository.save(post.toDomain(priority, member, parent))));
    }

    @ExecuteLog
    @PutMapping
    public ResponseEntity<?> putObjective(@RequestBody Objective.Put put) {
        /* PRIORITY 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Priority priority = priorityRepository.findByIdAndMemberEmail(put.getPriorityId(), put.getEmail())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'priorityId', 'email' parameters are not appropriate."));

        /* MEMBER 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Member member = memberRepository.findByEmail(put.getEmail())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        /* PARENT 로 연결한 OBJECTIVE 를 가져온다. 없다면 없는 상태로 Objective 생성 */
        Objective parent = objectiveRepository.findById(put.getParentId())
                .orElse(null);

        return ResponseEntity.status(HttpStatus.OK).body(Objective.Response.of(objectiveRepository.save(put.toDomain(priority, member, parent))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteObjective(@PathVariable("id") Long id) {
        objectiveRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.getReasonPhrase());
    }
}
