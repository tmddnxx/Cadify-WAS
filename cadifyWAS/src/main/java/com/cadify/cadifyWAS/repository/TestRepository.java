package com.cadify.cadifyWAS.repository;

import com.cadify.cadifyWAS.model.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<TestEntity, Long> {

}
