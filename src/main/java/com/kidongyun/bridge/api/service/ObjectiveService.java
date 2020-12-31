package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObjectiveService {
    private final ObjectiveRepository objectiveRepository;

    public void traversal(Objective root) {
        log.info(root.toString());
    }
}
