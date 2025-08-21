package com.corperate.TaskTracker.DTO.AdminTaskManagement;

import com.corperate.TaskTracker.Model.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDtoPost {
   private String description;
   private LocalDate lastDate;
   private Status status;
   private Integer assignedToId;
   private Integer createdById;



}
