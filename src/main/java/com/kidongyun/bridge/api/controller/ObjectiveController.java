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
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;

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
        return ResponseEntity.status(HttpStatus.OK).body(objectiveRepository.findByType(Cell.Type.Objective)
                .stream().map(Objective.Response::of).collect(toSet()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getObjectiveById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(Objective.Response.of(objectiveRepository.findByIdAndType(id, Cell.Type.Objective)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR))));
    }

    @ExecuteLog
    @PostMapping
    public ResponseEntity<?> postObjective(@RequestBody Objective.Post param) {
        /* PRIORITY 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Priority priority = priorityRepository.findByIdAndMemberEmail(param.getPriorityId(), param.getEmail())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        /* MEMBER 정보를 가져온다. 필수 정보이기 때문에 없다면 오류 반환 */
        Member member = memberRepository.findByEmail(param.getEmail())
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        /* PARENT 로 연결한 OBJECTIVE 를 가져온다. 없다면 없는 상태로 Objective 생성 */
        Objective parent = objectiveRepository.findById(param.getParentId())
                .orElse(null);

        return ResponseEntity.status(HttpStatus.OK).body(Objective.Response.of(objectiveRepository.save(param.toDomain(priority, member, parent))));
    }
}
