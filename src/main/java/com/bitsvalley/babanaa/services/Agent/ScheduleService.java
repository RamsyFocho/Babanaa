package com.bitsvalley.babanaa.services.Agent;

import com.bitsvalley.babanaa.domains.Agent.Schedule;
import com.bitsvalley.babanaa.domains.Agent.ScheduledTask;
import com.bitsvalley.babanaa.repositories.Agent.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;


    public List<Schedule> getAgentSchedules(Long agentId) {
        return scheduleRepository.findByAgentId(agentId);
    }

    public List<Schedule> getSchedulesByAgentAndDate(Long agentId, LocalDate date) {
        try {
            return scheduleRepository.findByAgentIdAndDate(agentId, date);
        } catch (Exception e) {
            // Log the error and handle it appropriately
            System.err.println("Error retrieving schedules for agent " + agentId + " on date " + date + ": " + e.getMessage());
            // Return an empty list or handle as needed
            return List.of();
        }
    }

    public List<Schedule> getSchedulesByAgent(Long agentId) {
        return scheduleRepository.findByAgentId(agentId);
    }

    public List<Schedule> getSchedulesByDate(LocalDate date) {
        return scheduleRepository.findByDate(date);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }



    public Schedule updateSchedule(Long id, Schedule schedule) {
        Optional<Schedule> existingSchedule = scheduleRepository.findById(id);
        if (existingSchedule.isPresent()) {
            Schedule updatedSchedule = existingSchedule.get();
            updatedSchedule.setAgent(schedule.getAgent());
            updatedSchedule.setDate(schedule.getDate());
            updatedSchedule.setTasks(schedule.getTasks());
            updatedSchedule.setNotes(schedule.getNotes());
            return scheduleRepository.save(updatedSchedule);
        } else {
            throw new EntityNotFoundException("Schedule not found with id: " + id);
        }
    }

    public Schedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Schedule not found with id: " + id));
    }

    public List<ScheduledTask> getTasksForSchedule(Long id) {
        Schedule schedule = getScheduleById(id);
        return schedule.getTasks();
    }

    public ScheduledTask addTaskToSchedule(Long id, ScheduledTask task) {
        Schedule schedule = getScheduleById(id);
        List<ScheduledTask> tasks = schedule.getTasks();
        tasks.add(task);
        schedule.setTasks(tasks);
        return scheduleRepository.save(schedule).getTasks().get(tasks.size() - 1);
    }

    public ScheduledTask updateTask(Long id, Long taskId, ScheduledTask task) {
        Schedule schedule = getScheduleById(id);
        List<ScheduledTask> tasks = schedule.getTasks();
        ScheduledTask existingTask = tasks.stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        existingTask.setTaskType(task.getTaskType());
        existingTask.setPlannedTime(task.getPlannedTime());
        existingTask.setStatus(task.getStatus());
        existingTask.setNotes(task.getNotes());
        return scheduleRepository.save(schedule).getTasks().stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
    }
    @Transactional
    public void deleteTask(Long id, Long taskId) {
        Schedule schedule = getScheduleById(id);
        List<ScheduledTask> tasks = schedule.getTasks();
        tasks.removeIf(t -> t.getId().equals(taskId));
        schedule.setTasks(tasks);
        scheduleRepository.save(schedule);
    }
}
