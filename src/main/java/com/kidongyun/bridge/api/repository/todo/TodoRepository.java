package com.kidongyun.bridge.api.repository.todo;

import com.kidongyun.bridge.api.entity.Todo;
import com.kidongyun.bridge.api.repository.cell.CellRepository;

import javax.transaction.Transactional;

@Transactional
public interface TodoRepository extends CellRepository<Todo>, TodoRepositoryCustom {
}
