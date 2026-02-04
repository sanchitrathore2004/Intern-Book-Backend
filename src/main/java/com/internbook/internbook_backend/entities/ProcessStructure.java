package com.internbook.internbook_backend.entities;

import com.internbook.internbook_backend.dtos.ProcessStructureRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
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

    public ProcessStructure(ProcessStructureRequestDto data) {
        this.setPiRound(data.getPiRounds());
        this.setOtRound(data.getOtRounds());
        this.setStartDate(data.getStartDate());
        this.setEndDate(data.getEndDate());
    }

    public void setSubmissions(Submission submission) {
        this.submissions = submission;
        this.id = submission != null ? submission.getId() : null;
    }
}