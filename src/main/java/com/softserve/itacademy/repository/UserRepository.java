package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select * from users", nativeQuery = true)
    List<User> findAll();

    @Query(value = "select * from users u where u.email = ?" , nativeQuery = true)
    Optional<User> findByEmail(String email);
}
