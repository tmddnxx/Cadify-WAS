package com.cadify.cadifyWAS.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    private String email;
    @Column(nullable = false)
    private String refreshToken;
    @Column(nullable = false)
    private boolean expired = false;

    public Token updateExpired(){
        this.expired = true;
        return this;
    }
}
