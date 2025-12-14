package com.internbook.internbook_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "process_structure")
public class ProcessStructure {
    @Id
    @Column(name = "submission_id")
    private Integer submissionId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "submission_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private Submission submission;

    @Column(name = "ot_round")
    private Integer otRound;

    @Column(name = "pi_round")
    private Integer piRound;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public void addSubmission(Submission submission) {
        this.submission = submission;
    }

}