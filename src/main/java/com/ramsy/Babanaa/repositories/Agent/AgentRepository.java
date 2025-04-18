package com.ramsy.Babanaa.repositories.Agent;

import com.ramsy.Babanaa.domains.Agent.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findByEmail(String email);

    Optional<Agent> findByPhoneNumber(String phoneNumber);

    Optional<Agent> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
