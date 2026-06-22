package com.ddaa.ddaaservice.repository;

import com.ddaa.ddaaservice.model.DdaaPagoNoUso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DdaaPagoNoUsoRepository extends JpaRepository<DdaaPagoNoUso, Integer> {

    void deleteByDdaa_Id(Integer ddaaId);
}
