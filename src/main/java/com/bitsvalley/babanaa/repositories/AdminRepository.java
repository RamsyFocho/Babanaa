package com.bitsvalley.babanaa.repositories;

import com.bitsvalley.babanaa.domains.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    Optional<Admin> findByEmailAndPassword(String email, String password);

}
