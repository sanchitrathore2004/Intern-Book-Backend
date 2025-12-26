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
@Table(name = "ctc_values")
public class CtcValue {
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

    @Column(name = "ctc")
    private BigDecimal ctc;

    @Column(name = "base_pay")
    private BigDecimal basePay;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="submission_id")
    private Submission submission;

    public CtcValue(User user, Company company, BigDecimal ctc, Submission submission) {
        this.user = user;
        this.company = company;
        this.ctc = ctc;
        this.submission = submission;
    }
}