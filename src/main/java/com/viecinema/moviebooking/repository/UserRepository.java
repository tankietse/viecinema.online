package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getUserByUsername(@Param("username") String username);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE (:username IS NULL OR u.username LIKE %:username%) AND (:email IS NULL OR u.email LIKE %:email%)")
    Page<User> findAllByFilters(@Param("username") String username, @Param("email") String email, Pageable pageable);
    
    @NotNull
    Optional<User> findById(Long id);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    List<User> findByLockedTrue();
}
