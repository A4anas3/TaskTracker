package com.corperate.TaskTracker.Service;

import com.corperate.TaskTracker.DTO.AdminManagerHistoryDto.AdminHistorySave;
import com.corperate.TaskTracker.DTO.AdminTaskManagement.TaskDTOGet;
import com.corperate.TaskTracker.DTO.AdminTaskManagement.TaskDtoPost;
import com.corperate.TaskTracker.Model.*;
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
@Service
@AllArgsConstructor
@Transactional
public class TaskService {
    @Autowired
    private final TaskhistoryRepo taskhistoryRepo;
    @Autowired
    private final UserRepo repo;
    @Autowired
    private final TaskRepo taskRepo;
    public String createTask(TaskDtoPost dto) {


        User createdBy=repo.findById(dto.getCreatedById()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"no created id found in db!Please register yourself first"+dto.getCreatedById()));
        User AssignedTo=repo.findById(dto.getAssignedToId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"no created id found in db!Please register yourself first"+dto.getAssignedToId()));
        if(AssignedTo.getRole()== Role.ADMIN){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Only Employees and Manager assign task");
        }


        tasks task=new tasks();
        if(createdBy.getRole()==Role.ADMIN || createdBy.getRole()==Role.MANAGER){
            task.setCreatedBy(createdBy);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"only Admin and Manager can assign Task");
        }

        task.setAssignedTo(AssignedTo);
        task.setStatus(dto.getStatus()!=null?dto.getStatus(): Status.PENDING);
        task.setLastDate(dto.getLastDate());
        task.setDescription(dto.getDescription());

        taskRepo.save(task);
        saveHistory(task,createdBy, ActionType.ASSIGNED,"Task Assigned");
        return ("Successfully Assign task to "+AssignedTo.getFirstname());

    }


    public String updateTask(Long id, TaskDtoPost dto) {
        tasks exist= taskRepo.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Person with "+id+"NotFound")
        );

        if(dto.getStatus()!=null){
            exist.setStatus(dto.getStatus());
        }
        if(dto.getDescription()!=null){
            exist.setDescription(dto.getDescription());

        }
        if(dto.getLastDate()!=null){
            exist.setLastDate(dto.getLastDate());

        }
        if(dto.getAssignedToId()!=null){
            User createdBy=repo.findById(dto.getCreatedById()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"no created id found in db!Please register yourself first"+dto.getCreatedById()));
            exist.setCreatedBy(createdBy);


        }
        if(dto.getCreatedById()!=null){
            User AssignedTo=repo.findById(dto.getAssignedToId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"no created id found in db!Please register yourself first"+dto.getAssignedToId()));
            exist.setAssignedTo(AssignedTo);
        }
        User createdBy=repo.findById(dto.getCreatedById()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"no created id found in db!Please register yourself first"+dto.getCreatedById()));
        taskRepo.save(exist);
        saveHistory(exist,createdBy,ActionType.REASSIGNED,"Succesfully Update ");
        return ("Task Detail Updated");



    }

    public String deleteTask(Long id) throws IOException {
        if(!taskRepo.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"task with "+id+"not found");

        }
        taskRepo.deleteById(id);

        return ("Succesfully deleted the task with id "+id);
    }

    public List<TaskDTOGet> GetAllTask() throws IOException{
        List<tasks>t=taskRepo.findAll();
        return t.stream()
                .map((a)->new TaskDTOGet(
                        a.getId(),
                        a.getGivenDate(),
                        a.getDescription(),  // description comes 3rd
                        a.getLastDate(),     // lastDate comes 4th
                        a.getStatus(),
                        a.getAssignedTo()!=null?a.getAssignedTo().getId():null,
                        a.getCreatedBy()!=null?a.getCreatedBy().getId():null
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

        a.setAssignedToId(t.getAssignedTo()!=null?t.getAssignedTo().getId():null);
        a.setCreatedById(t.getCreatedBy()!=null?t.getCreatedBy().getId():null);

        return a;

    }

    public void saveHistory(tasks taskId, User actionBy, ActionType actionType,String remarks){
        Taskhistory task = new Taskhistory();
        task.setTask(taskId);
        task.setActionBy(actionBy);
        task.setAction(actionType);
        task.setRemarks(remarks);


        taskhistoryRepo.save(task);

    }


    public List<AdminHistorySave> History() {
        List<Taskhistory> task= taskhistoryRepo.findAll();

        return task.stream()
                .map(e->new AdminHistorySave(
                        e.getId(),
                        e.getTask().getId(),
                        e.getActionBy().getId(),
                        e.getActionBy()!=null?e.getActionBy().getFirstname():null,
                        e.getAction(),
                        e.getActionTime(),
                        e.getRemarks()

                )).toList();

    }

    public List<AdminHistorySave> TaskHistoryById(Long taskId) {
        List<Taskhistory> task = taskhistoryRepo.findByTaskId(taskId);
        if(task==null){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND,"Task with id" + taskId +"Not Found");


        }
        return task.stream()
                .map(e->new AdminHistorySave(
                        e.getId(),
                        e.getTask().getId(),
                        e.getActionBy().getId(),
                        e.getActionBy()!=null?e.getActionBy().getFirstname():null,
                        e.getAction(),
                        e.getActionTime(),
                        e.getRemarks()

                )).toList();

    }
}
