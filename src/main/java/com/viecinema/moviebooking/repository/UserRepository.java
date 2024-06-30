package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getUserByUsername(@Param("username") String username);

    Optional<User> findByUsername(String username);
    @NotNull
    Optional<User> findById(Integer id);

    boolean existsByUsername(String username);

    List<User> findByLockedTrue();
}
