package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.entity.Objective;
import com.kidongyun.bridge.api.repository.cell.CellRepository;
import com.kidongyun.bridge.api.repository.objective.ObjectiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class ObjectiveService extends CellService<Objective> {
    private ObjectiveRepository objectiveRepository;

    @Autowired
    public ObjectiveService(CellRepository<Objective> cellRepository, ObjectiveRepository objectiveRepository) {
        super(cellRepository);
        this.objectiveRepository = objectiveRepository;
    }

    public Set<Objective> findByObjective(Objective objective) {
        if(Objects.isNull(objective)) {
            return Set.of();
        }

        if(Objects.nonNull(objective.getId())) {
            Optional<Objective> response = objectiveRepository.findById(objective.getId());

            if(response.isPresent()) {
                return Set.of(response.get());
            }
        }

        if(Objects.nonNull(objective.getMember()) && Objects.nonNull(objective.getMember().getEmail())) {
            if(Objects.nonNull(objective.getPriority()) && Objects.nonNull(objective.getPriority().getId())) {
                if(Objects.nonNull(objective.getParent()) && Objects.nonNull(objective.getParent().getId())) {

                    if(Objects.nonNull(objective.getStatus())) {
                        // return find by email and priorityId and parentId and status
                        return Set.of();
                    } else {
                        // return find by email and priorityId and parentId
                        return Set.of();
                    }
                } else {
                    if(Objects.nonNull(objective.getStatus())) {
                        // return find by email and priorityId and status
                        return Set.of();
                    } else {
                        // return find by email and priorityId
                        return Set.of();
                    }
                }
            } else {
                if(Objects.nonNull(objective.getParent()) && Objects.nonNull(objective.getParent().getId())) {
                    if(Objects.nonNull(objective.getStatus())) {
                        // return find by email and parentId and status
                        return Set.of();
                    } else {
                        // return find by email and parentId
                        return Set.of();
                    }
                } else {
                    if(Objects.nonNull(objective.getStatus())) {
                        // return find by email and status
                        return Set.of();
                    } else {
                        // return find by email
                        return Set.of();
                    }
                }
            }
        } else {
            if(Objects.nonNull(objective.getPriority()) && Objects.nonNull(objective.getPriority().getId())) {
                if(Objects.nonNull(objective.getParent()) && Objects.nonNull(objective.getParent().getId())) {
                    if(Objects.nonNull(objective.getStatus())) {
                        // return find by priorityId and parentId and status
                        return Set.of();
                    } else {
                        // return find by priorityId and parentId
                        return Set.of();
                    }
                } else {
                    if(Objects.nonNull(objective.getStatus())) {
                        // return find by priorityId and status
                        return Set.of();
                    } else {
                        // return find by priorityId
                        return Set.of();
                    }
                }
            } else {
                if(Objects.nonNull(objective.getParent()) && Objects.nonNull(objective.getParent().getId())) {
                    if(Objects.nonNull(objective.getStatus())) {
                        // return find by parentId and status
                        return Set.of();
                    } else {
                        // return find by parentId
                        return Set.of();
                    }
                } else {
                    if(Objects.nonNull(objective.getStatus())) {
                        // return find by status
                        return Set.of();
                    } else {
                        // return empty
                        return Set.of();
                    }
                }
            }
        }
    }
}
