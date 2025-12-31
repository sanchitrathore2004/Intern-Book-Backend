package com.internbook.internbook_backend.entities;

import com.internbook.internbook_backend.enums.Degree;
import com.internbook.internbook_backend.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "roll_no")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "degree")
    @Enumerated(EnumType.STRING)
    private Degree degree;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "year")
    private Integer year;

    @Column(name = "profile_url", insertable = false)
    private String profileUrl;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private Set<CtcValue> ctcValues = new LinkedHashSet<>();

    @Column(name = "user_role", insertable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "is_anonymous")
    private boolean isAnonymous;

    @ManyToOne
    @JoinColumn(name = "favourite_company_id")
    private Company favouriteCompany;

    @ManyToMany
    @JoinTable(
            name = "favourites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "submission_id")
    )
    private Set<Submission> favouriteSubmissions = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "submission_id")
    )
    private Set<Submission> likedSubmissions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Report> reports = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Stipend> stipends = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Submission> submissions = new LinkedHashSet<>();

}