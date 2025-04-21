package com.bitsvalley.babanaa.controllers.Agent;

import com.bitsvalley.babanaa.domains.Agent.Schedule;
import com.bitsvalley.babanaa.domains.Agent.ScheduledTask;
import com.bitsvalley.babanaa.services.Agent.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api//agent/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public List<Schedule> getSchedules(@RequestParam(name = "agentId", required = false) Long agentId,
                                       @RequestParam(name = "date", required = false) LocalDate date) {
        if (agentId != null && date != null) {
            return scheduleService.getSchedulesByAgentAndDate(agentId, date);
        } else if (agentId != null) {
            return scheduleService.getSchedulesByAgent(agentId);
        } else if (date != null) {
            return scheduleService.getSchedulesByDate(date);
        } else {
            return scheduleService.getAllSchedules();
        }
    }

    @PostMapping
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        return scheduleService.createSchedule(schedule);
    }

    @GetMapping("/{id}")
    public Schedule getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id);
    }

    @PutMapping("/{id}")
    public Schedule updateSchedule(@PathVariable Long id, @RequestBody Schedule schedule) {
        return scheduleService.updateSchedule(id,schedule);
    }

//    ----------------schedule task----------------------

    @GetMapping("/{id}/tasks")
    public List<ScheduledTask> getTasksForSchedule(@PathVariable Long id) {
        return scheduleService.getTasksForSchedule(id);
    }

    @PostMapping("/{id}/tasks")
    public ScheduledTask addTaskToSchedule(@PathVariable Long id, @RequestBody ScheduledTask task) {
        return scheduleService.addTaskToSchedule(id, task);
    }

    @PutMapping("/{id}/tasks/{taskId}")
    public ScheduledTask updateTask(@PathVariable Long id, @PathVariable Long taskId, @RequestBody ScheduledTask task) {
        return scheduleService.updateTask(id, taskId, task);
    }

    @DeleteMapping("/{id}/tasks/{taskId}")
    public void deleteTask(@PathVariable Long id, @PathVariable Long taskId) {
        scheduleService.deleteTask(id, taskId);
    }
}
