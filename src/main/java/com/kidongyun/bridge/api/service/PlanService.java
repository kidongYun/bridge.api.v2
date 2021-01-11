package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.entity.Plan;
import com.kidongyun.bridge.api.repository.cell.CellRepository;
import com.kidongyun.bridge.api.repository.plan.PlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class PlanService extends CellService<Plan> {
    private PlanRepository planRepository;

    public PlanService(CellRepository<Plan> cellRepository, PlanRepository planRepository) {
        super(cellRepository);
        this.planRepository = planRepository;
    }

    public Set<Plan> findByObjectiveId(Long id) {
        if(Objects.isNull(id)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'id' should be not null");
        }

        return planRepository.findByObjectiveId(id);
    }
}
