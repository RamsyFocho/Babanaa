package com.bitsvalley.babanaa.services.Agent;

import com.bitsvalley.babanaa.domains.Agent.Schedule;
import com.bitsvalley.babanaa.repositories.Agent.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;


    public List<Schedule> getAgentSchedules(Long agentId) {
        return scheduleRepository.findByAgentId(agentId);
    }
}
