package com.corperate.TaskTracker.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Taskhistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private tasks task;

    @ManyToOne
    @JoinColumn(name = "action_by", nullable = false)
    private User actionBy;

    @Enumerated(EnumType.STRING)
    private ActionType action;
    @Column(nullable = false,updatable = false)
    @CreationTimestamp
    private LocalDateTime actionTime;

    @Column(length = 2000)
    private String remarks;



}
