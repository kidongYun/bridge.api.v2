package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.entity.Priority;
import com.kidongyun.bridge.api.repository.priority.PriorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class PriorityService {
    private PriorityRepository priorityRepository;

    @Autowired
    public PriorityService(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    public Priority findByIdAndMemberEmail(Long priorityId, String email) throws Exception {
        if(Objects.isNull(priorityId) && Objects.isNull(email)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'priorityId' or 'email' parameters are not appropriate");
        }

        if(Objects.nonNull(priorityId) && Objects.isNull(email)) {
            return priorityRepository.findById(priorityId).orElseThrow(Exception::new);
        }

        if(Objects.isNull(priorityId)) {
            return priorityRepository.findByMemberEmail(email).iterator().next();
        }

        return priorityRepository.findByIdAndMemberEmail(priorityId, email).orElseThrow(Exception::new);
    }

    public Set<Priority> findByMemberEmail(String email) throws Exception {
        if(Objects.isNull(email)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "'email' should be not null");
        }

        return priorityRepository.findByMemberEmail(email);
    }

    public List<Priority> findAll() {
        return priorityRepository.findAll();
    }
}
