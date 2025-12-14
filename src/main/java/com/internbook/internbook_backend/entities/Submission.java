package com.internbook.internbook_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@Table(name = "submissions")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public void addUser(User user) {
        this.user = user;
    }

    public void addCompany(Company company) {
        this.company = company;
    }

    public void addRole(Role role) {
        this.role = role;
    }

    public enum Type {
        intern,
        placed,
        intern_to_ppo
    };
}
