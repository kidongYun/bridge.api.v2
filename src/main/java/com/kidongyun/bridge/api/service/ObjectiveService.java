package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.repository.cell.CellRepository;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ObjectiveService extends CellService<Objective> {
    private ObjectiveRepository objectiveRepository;

    @Autowired
    public ObjectiveService(CellRepository<Objective> cellRepository, ObjectiveRepository objectiveRepository) {
        super(cellRepository);
        this.objectiveRepository = objectiveRepository;
    }

    public List<Objective> findByObjective(Objective objective) {
        return objectiveRepository.findByObjective(objective);
    }
}
