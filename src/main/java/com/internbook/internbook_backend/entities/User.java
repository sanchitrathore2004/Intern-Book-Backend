package com.internbook.internbook_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @Column(name = "rollNo")
    private Long rollNo;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "degree", columnDefinition = "enum('BTECH', 'MTECH', 'MCA')")
    private Degree degree;

    @Column(name = "year")
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "department_id")
    @ToString.Exclude
    private Department department;

    public void addDepartment(Department department) {
        this.department = department;
    }

    public enum Degree {
        BTECH,
        MTECH,
        MCA
    };
}
