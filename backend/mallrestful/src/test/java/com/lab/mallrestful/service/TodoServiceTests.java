package com.lab.mallrestful.service;

import com.lab.mallrestful.dto.TodoDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@Log4j2
public class TodoServiceTests {
    @Autowired
    private TodoService todoService;

    @Test
    public void testRegister() {
        TodoDTO todoDTO = TodoDTO.builder()
                .title("서비스 테스트")
                .author("tester")
                .dueDate(LocalDate.of(2024,8,23))
                .build();

        Long tno = todoService.register(todoDTO);
        log.info("TNO: " + tno);
    }

    @Test
    public void testGet() {
        Long tno = 101L;
        TodoDTO todoDTO = todoService.get(tno);
        log.info(todoDTO);
    }
}
