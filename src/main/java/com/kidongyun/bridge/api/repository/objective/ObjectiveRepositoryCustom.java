package com.kidongyun.bridge.api.repository.objective;

import com.kidongyun.bridge.api.entity.Objective;

import java.util.List;

public interface ObjectiveRepositoryCustom {
    List<Objective> findByObjective(Objective obj);
}
