package com.cadify.cadifyWAS.repository;

import com.cadify.cadifyWAS.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
    boolean existsByEmail(String email);
    Token findByEmail(String email);
}
