package com.cadify.cadifyWAS.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity(name = "testEntity")
@Getter
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;
    @Column
    private String carrier;
    @Column
    private String number;
}
