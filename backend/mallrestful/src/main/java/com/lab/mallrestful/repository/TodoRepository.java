package com.lab.mallrestful.repository;

import com.lab.mallrestful.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo,Long> {
}
