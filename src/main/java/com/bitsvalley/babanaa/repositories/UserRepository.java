package com.bitsvalley.babanaa.repositories;

import com.bitsvalley.babanaa.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByPassword(String password);
//    @Query("SELECT l.levelName FROM Level l WHERE l.levelName =:level")
//    Optional<Level> findLevelByName(@Param("level") String level);

    @Query("SELECT u FROM User u WHERE u.phoneNumber=?1 AND u.password=?2")
    Optional<User> findByCredentials(String phoneNumber,String password);

    List<User> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
