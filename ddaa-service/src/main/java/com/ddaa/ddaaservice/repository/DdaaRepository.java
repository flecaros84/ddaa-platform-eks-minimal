package com.ddaa.ddaaservice.repository;

import com.ddaa.ddaaservice.model.Ddaa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DdaaRepository extends JpaRepository<Ddaa, Integer> {
}
