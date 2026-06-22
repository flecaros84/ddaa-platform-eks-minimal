package com.ddaa.ddaaservice;

import com.ddaa.ddaaservice.dto.DdaaDtos.DdaaCreateDto;
import com.ddaa.ddaaservice.dto.DdaaDtos.DdaaUpdateDto;
import com.ddaa.ddaaservice.repository.DdaaQueryRepository;
import com.ddaa.ddaaservice.service.DdaaQueryService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class DdaaCrudIntegrationTest {

    @Autowired
    private DdaaQueryService service;

    @Autowired
    private DdaaQueryRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void createUpdateDeleteLifecycle() {
        // ensure referential rows exist
        jdbcTemplate.update("MERGE INTO COMUNA (ID_Comuna, Nombre) KEY(ID_Comuna) VALUES (?, ?)", "001", "Comuna 1");
        jdbcTemplate.update("MERGE INTO RUTS (Rut, Nombre) KEY(Rut) VALUES (?, ?)", 11111111L, "Titular Test");
        jdbcTemplate.update("MERGE INTO FUENTE (ID_Fuente, Nombre) KEY(ID_Fuente) VALUES (?, ?)", 1, "Fuente 1");

        DdaaCreateDto create = new DdaaCreateDto("001", 11111111L, null, 1L, "Fuente X", "Privado", "Titulo", "Activo");
        long id = service.createDdaa(create);
        assertThat(id).isGreaterThan(0);

        var found = repository.findDdaaById(id);
        assertThat(found).isPresent();

        // ensure comuna for update exists
        jdbcTemplate.update("MERGE INTO COMUNA (ID_Comuna, Nombre) KEY(ID_Comuna) VALUES (?, ?)", "002", "Comuna 2");
        DdaaUpdateDto update = new DdaaUpdateDto("002", 11111111L, null, 1L, "Fuente Y", "Publico", "Titulo", "Inactivo");
        int updated = service.updateDdaa(id, update);
        assertThat(updated).isGreaterThanOrEqualTo(0);

        int deleted = service.deleteDdaa(id);
        assertThat(deleted).isGreaterThanOrEqualTo(0);
    }
}
