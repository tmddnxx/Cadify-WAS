package com.cadify.cadifyWAS.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    private String email;
    @Column(nullable = false)
    private String refreshToken;
}
