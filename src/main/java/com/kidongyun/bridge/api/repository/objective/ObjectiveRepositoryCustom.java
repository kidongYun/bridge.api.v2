package com.kidongyun.bridge.api.repository.objective;

import com.kidongyun.bridge.api.entity.Objective;

import java.util.Optional;

public interface ObjectiveRepositoryCustom {
    Optional<Objective> findById(Long id);
}
