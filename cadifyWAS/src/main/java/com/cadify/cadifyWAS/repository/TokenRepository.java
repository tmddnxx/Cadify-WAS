package com.cadify.cadifyWAS.repository;

import com.cadify.cadifyWAS.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
    Token findByEmail(String email);
    void deleteByEmail(String email);
}
