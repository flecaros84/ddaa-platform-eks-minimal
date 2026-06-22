package com.ddaa.ddaaservice.repository;

import com.ddaa.ddaaservice.model.DdaaEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DdaaEjercicioRepository extends JpaRepository<DdaaEjercicio, Integer> {

    List<DdaaEjercicio> findByDdaa_Id(Integer ddaaId);

    void deleteByDdaa_Id(Integer ddaaId);
}
