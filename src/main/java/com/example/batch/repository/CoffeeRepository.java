package com.example.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.batch.repository.entity.Coffee;

@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

}
