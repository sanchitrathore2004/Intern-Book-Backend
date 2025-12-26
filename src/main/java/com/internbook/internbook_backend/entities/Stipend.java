package com.internbook.internbook_backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "stipends")
public class Stipend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "stipend")
    private BigDecimal stipend;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="submission_id")
    private Submission submission;

    public Stipend(User user, Company company, BigDecimal stipend, Submission submission) {
        this.user = user;
        this.company = company;
        this.stipend = stipend;
        this.submission = submission;
    }
}