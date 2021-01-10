package com.kidongyun.bridge.api.service;

import com.kidongyun.bridge.api.config.QuerydslConfig;
import com.kidongyun.bridge.api.repository.cell.CellRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(QuerydslConfig.class)
public class CellServiceTest {
    @Mock
    CellRepository cellRepository;
    @InjectMocks
    CellService cellServiceMock;
    @Autowired
    CellService cellService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test(expected = HttpClientErrorException.class)
    public void findByType_typeIsNull() {
        /* Arrange, Act, Assert */
        cellService.findByType(null);
    }

    @Test
    public void findByType_objective_normalCase() {

    }
}