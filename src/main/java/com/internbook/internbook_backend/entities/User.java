package com.internbook.internbook_backend.entities;

import com.internbook.internbook_backend.enums.Degree;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private Set<CtcValue> ctcValues = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "favouriteBy")
    private Set<Submission> favouriteSubmissions = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "likedBy")
    private Set<Submission> likedSubmissions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Report> reports = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Stipend> stipends = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Submission> submissions = new LinkedHashSet<>();

}