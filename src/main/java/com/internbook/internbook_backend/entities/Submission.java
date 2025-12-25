package com.internbook.internbook_backend.entities;

import com.internbook.internbook_backend.enums.SubmissionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@DynamicInsert
@Table(name = "submissions")
public class Submission {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private SubmissionType type;

    @Column(name = "benefits")
    private String benefits;

    @Column(name = "experience")
    private String experience;

    @Column(name = "mistakes")
    private String mistakes;

    @Column(name = "resources")
    private String resources;

    @Column(name = "cgpa")
    private BigDecimal cgpa;

    @Column(name = "club_positions")
    private String clubPositions;

    @Column(name = "approved")
    private Boolean approved;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_offensive")
    private Boolean isOffensive;

    @Column(name = "is_fraud")
    private Boolean isFraud;

    @Column(name = "like_count")
    private Integer likeCount;

    @ManyToMany(mappedBy = "favouriteSubmissions")
    private Set<User> favouriteBy = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "likedSubmissions")
    private Set<User> likedBy = new LinkedHashSet<>();

    @OneToOne(mappedBy = "submissions", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private ProcessStructure processStructure;

    @OneToMany(mappedBy = "submission")
    private Set<Report> reports = new LinkedHashSet<>();

    @OneToOne (mappedBy = "submission")
    private Stipend stipend;

    @OneToOne (mappedBy = "submission")
    private CtcValue ctcValue;

}