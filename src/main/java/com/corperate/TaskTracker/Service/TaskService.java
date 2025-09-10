package com.corperate.TaskTracker.Service;

import com.corperate.TaskTracker.DTO.AdminManagerHistoryDto.AdminHistorySave;
import com.corperate.TaskTracker.DTO.AdminTaskManagement.TaskDTOGet;
import com.corperate.TaskTracker.DTO.AdminTaskManagement.TaskDtoPost;
import com.corperate.TaskTracker.Model.*;
import com.corperate.TaskTracker.Model.Principal.UserDetailPrincipal;
import com.corperate.TaskTracker.Repository.AdminRepoOfALL.UserRepo;
import com.corperate.TaskTracker.Repository.HistryTrack.TaskhistoryRepo;
import com.corperate.TaskTracker.Repository.TaskRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

import static com.corperate.TaskTracker.Model.ActionType.IN_PROGRESS;
import static com.corperate.TaskTracker.Model.Status.COMPLETED;

@Service
@AllArgsConstructor
@Transactional
public class TaskService {

    private final TaskhistoryRepo taskhistoryRepo;

    private final UserRepo repo;
    @Autowired
    private TaskRepo taskRepo;

    public String createTask(TaskDtoPost dto) {


        User createdBy = repo.findById(dto.getCreatedById()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no created id found in db!Please register yourself first" + dto.getCreatedById()));
        User AssignedTo = repo.findById(dto.getAssignedToId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no created id found in db!Please register yourself first" + dto.getAssignedToId()));
        if (AssignedTo.getRole() == Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only Employees and Manager assign task");
        }


        tasks task = new tasks();
        if (createdBy.getRole() == Role.ADMIN || createdBy.getRole() == Role.MANAGER) {
            task.setCreatedBy(createdBy);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "only Admin and Manager can assign Task");
        }

        task.setAssignedTo(AssignedTo);
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : Status.PENDING);
        task.setLastDate(dto.getLastDate());
        task.setDescription(dto.getDescription());

        taskRepo.save(task);
        saveHistory(task, createdBy, ActionType.ASSIGNED, "Task Assigned");
        return ("Successfully Assign task to " + AssignedTo.getFirstname());

    }


    public String updateTask(Long id, TaskDtoPost dto) {
        tasks exist = taskRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person with " + id + "NotFound")
        );

        if (dto.getStatus() != null) {
            exist.setStatus(dto.getStatus());
        }
        if (dto.getDescription() != null) {
            exist.setDescription(dto.getDescription());

        }
        if (dto.getLastDate() != null) {
            exist.setLastDate(dto.getLastDate());

        }
        if (dto.getAssignedToId() != null) {
            User createdBy = repo.findById(dto.getCreatedById()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no created id found in db!Please register yourself first" + dto.getCreatedById()));
            exist.setCreatedBy(createdBy);


        }
        if (dto.getCreatedById() != null) {
            User AssignedTo = repo.findById(dto.getAssignedToId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no created id found in db!Please register yourself first" + dto.getAssignedToId()));
            exist.setAssignedTo(AssignedTo);
        }
        User createdBy = repo.findById(dto.getCreatedById()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no created id found in db!Please register yourself first" + dto.getCreatedById()));
        taskRepo.save(exist);
        saveHistory(exist, createdBy, ActionType.REASSIGNED, "Succesfully Update ");
        return ("Task Detail Updated");


    }

    public String deleteTask(Long id, UserDetailPrincipal principal) throws IOException {
        // 1. Find the task before deleting it
        tasks taskToDelete = taskRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with id " + id + " not found"));

        // 2. Get the user who is performing the action
        User actionBy = principal.getUser();

        // 3. Save the history record BEFORE deleting the task
        saveHistory(taskToDelete, actionBy, ActionType.DELETED, "Task has been deleted");

        // 4. Now, delete the task
        taskRepo.deleteById(id);

        return "Successfully deleted the task with id " + id;
    }

    public List<TaskDTOGet> GetAllTask() throws IOException {
        List<tasks> t = taskRepo.findAll();
        return t.stream()
                .map((a) -> new TaskDTOGet(
                        a.getId(),
                        a.getGivenDate(),
                        a.getDescription(),  // description comes 3rd
                        a.getLastDate(),     // lastDate comes 4th
                        a.getStatus(),
                        a.getAssignedTo() != null ? a.getAssignedTo().getId() : null,
                        a.getCreatedBy() != null ? a.getCreatedBy().getId() : null
                )).toList();


    }

    public TaskDTOGet GetTaskById(Long id) throws IOException {

        tasks t = taskRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "task with this id" + id + "not present in DB")
        );
        TaskDTOGet a = new TaskDTOGet();
        a.setDescription(t.getDescription());
        a.setStatus(t.getStatus());
        a.setTaskId(t.getId());
        a.setGivenDate(t.getGivenDate());
        a.setLastDate(t.getLastDate());

        a.setAssignedToId(t.getAssignedTo() != null ? t.getAssignedTo().getId() : null);
        a.setCreatedById(t.getCreatedBy() != null ? t.getCreatedBy().getId() : null);

        return a;

    }

    public void saveHistory(tasks taskId, User actionBy, ActionType actionType, String remarks) {
        Taskhistory task = new Taskhistory();
        task.setTask(taskId);
        task.setActionBy(actionBy);
        task.setAction(actionType);
        task.setRemarks(remarks);


        taskhistoryRepo.save(task);

    }


    public List<AdminHistorySave> History() {
        List<Taskhistory> task = taskhistoryRepo.findAll();

        return task.stream()
                .map(e -> new AdminHistorySave(
                        e.getId(),
                        e.getTask().getId(),
                        e.getActionBy().getId(),
                        e.getActionBy() != null ? e.getActionBy().getFirstname() : null,
                        e.getAction(),
                        e.getActionTime(),
                        e.getRemarks()

                )).toList();

    }

    public List<AdminHistorySave> TaskHistoryById(Long taskId) {
        List<Taskhistory> task = taskhistoryRepo.findByTaskId(taskId);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task with id" + taskId + "Not Found");


        }
        return task.stream()
                .map(e -> new AdminHistorySave(
                        e.getId(),
                        e.getTask().getId(),
                        e.getActionBy().getId(),
                        e.getActionBy() != null ? e.getActionBy().getFirstname() : null,
                        e.getAction(),
                        e.getActionTime(),
                        e.getRemarks()

                )).toList();

    }

    public List<TaskDTOGet> getMyTask(Integer s) {
        List<tasks> temp = taskRepo.findByAssignedTo_Id(s);
        return temp.stream()
                .map((e) -> new TaskDTOGet(
                        e.getId(),
                        e.getGivenDate(),
                        e.getDescription(),
                        e.getLastDate(),

                        e.getStatus(),
                        e.getAssignedTo() == null ? e.getAssignedTo().getId() : null,
                        e.getCreatedBy().getId()
                )).toList();
    }

    public List<TaskDTOGet> AssignedByMe(Integer s) {
        List<tasks> a = taskRepo.findByCreatedBy_Id(s);
        return a.stream()
                .map((e) -> new TaskDTOGet(
                        e.getId(),
                        e.getGivenDate(),
                        e.getDescription(),
                        e.getLastDate(),
                        e.getStatus(),
                        e.getAssignedTo() == null ? e.getAssignedTo().getId() : null,
                        e.getCreatedBy() == null ? e.getCreatedBy().getId() : null
                )).toList();
    }

    public TaskDTOGet UpdateStatus(long taskId, String status, int id) {
        tasks t = taskRepo.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "task not exist"));
        if (t.getAssignedTo() == null || t.getAssignedTo().getId() != id) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed ");
        }
        Status newStatus;
        try {
            newStatus= Status.valueOf(status.toUpperCase());
            t.setStatus(newStatus);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status: " + status);
        }
        taskRepo.save(t);

        ActionType actionType;
        switch (newStatus) {
            case IN_PROGRESS:
                actionType = IN_PROGRESS;
                break;
            case COMPLETED:
                actionType = ActionType.COMPLETED;
                break;
            default:
                actionType = ActionType.UPDATED;
                break;
        }
        User actionBy = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        saveHistory(t, actionBy, ActionType.UPDATED, "Status changed to " + status);

        TaskDTOGet taskDTOGet = new TaskDTOGet(
                t.getId(),
                t.getGivenDate(),
                t.getDescription(),
                t.getLastDate(),
                t.getStatus(),
                t.getAssignedTo() != null ? t.getAssignedTo().getId() : null,
                t.getCreatedBy() != null ? t.getCreatedBy().getId() : null
        );
        return taskDTOGet;
    }
}


