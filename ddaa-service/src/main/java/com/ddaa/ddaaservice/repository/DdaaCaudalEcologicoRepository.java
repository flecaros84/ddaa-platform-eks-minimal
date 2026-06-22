package com.ddaa.ddaaservice.repository;

import com.ddaa.ddaaservice.model.DdaaCaudalEcologico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DdaaCaudalEcologicoRepository extends JpaRepository<DdaaCaudalEcologico, Integer> {

    void deleteByEjercicio_Id(Integer ejercicioId);
}
