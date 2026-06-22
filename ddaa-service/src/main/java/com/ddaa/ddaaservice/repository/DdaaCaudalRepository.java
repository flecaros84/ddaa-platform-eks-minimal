package com.ddaa.ddaaservice.repository;

import com.ddaa.ddaaservice.model.DdaaCaudal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DdaaCaudalRepository extends JpaRepository<DdaaCaudal, Integer> {

    void deleteByEjercicio_Id(Integer ejercicioId);
}
