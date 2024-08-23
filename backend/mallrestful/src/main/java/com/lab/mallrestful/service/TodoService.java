package com.lab.mallrestful.service;

import com.lab.mallrestful.dto.PageRequestDTO;
import com.lab.mallrestful.dto.PageResponseDTO;
import com.lab.mallrestful.dto.TodoDTO;

public interface TodoService {
    Long register(TodoDTO todoDTO); // 등록 메서드
    TodoDTO get(Long tno);  // TodoDTO를 반환하는 조회용 메서드
    void modify(TodoDTO todoDTO); // 수정 메서드
    void remove(Long tno); // 삭제 메서드
    PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO);
}
