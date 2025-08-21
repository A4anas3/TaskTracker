package com.corperate.TaskTracker.DTO.AdminManagerHistoryDto;

import com.corperate.TaskTracker.Model.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AdminHistorySave {
    private Long id;
    private Long taskid;
//    private String actionByNAme;
    private  Integer actionBy;
    private String actionByName;
    private ActionType actionType;
    private LocalDateTime actionTime;

    private String remarks;

}
