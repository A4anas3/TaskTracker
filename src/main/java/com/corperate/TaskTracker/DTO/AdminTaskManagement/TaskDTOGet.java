package com.corperate.TaskTracker.DTO.AdminTaskManagement;

import com.corperate.TaskTracker.Model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTOGet {
        private Long taskId;
        private LocalDate givenDate;

        private String description;
        private LocalDate lastDate;
        private Status status;
        private Integer assignedToId;
        private Integer createdById;



    }


