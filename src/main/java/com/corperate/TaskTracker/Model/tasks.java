package com.corperate.TaskTracker.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class tasks {
    @Id
    @SequenceGenerator(name = "task_seq", sequenceName="task_seq",
    initialValue = 32456,
    allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,
    generator = "task_seq")
    private long id;
    private LocalDate givenDate=LocalDate.now();
    @Enumerated(EnumType.STRING)
   private Status status=Status.PENDING;
    private LocalDate lastDate;
    private String description;
    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;
    @ManyToOne
    @JoinColumn(name = "createdby",nullable = false)
    private User CreatedBy;



}
