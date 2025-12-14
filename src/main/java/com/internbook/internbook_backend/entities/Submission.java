package com.internbook.internbook_backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "enum('intern', 'place', 'intern_to_ppo')")
    private Type type;

    @Column(name = "benefits")
    private String benefits;

    @Column(name = "experience")
    private String experience;

    @Column(name = "mistakes")
    private String mistakes;

    @Column(name = "resources")
    private String resources;

    @Column(name = "cgpa")
    private Float cgpa;

    @Column(name = "club_positions")
    private String clubPositions;

    public enum Type {
        intern,
        placed,
        intern_to_ppo
    };
}
