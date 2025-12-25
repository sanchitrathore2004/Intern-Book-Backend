package com.internbook.internbook_backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "process_structure")
public class ProcessStructure {
    @Id
    @Column(name = "submission_id")
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submissions;

    @Column(name = "ot_round")
    private Integer otRound;

    @Column(name = "pi_round")
    private Integer piRound;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

}