package com.lab.mallrestful.service;

import com.lab.mallrestful.domain.Todo;
import com.lab.mallrestful.dto.PageRequestDTO;
import com.lab.mallrestful.dto.PageResponseDTO;
import com.lab.mallrestful.dto.TodoDTO;
import com.lab.mallrestful.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor // 생성자 자동 주입
public class TodoServiceImpl implements TodoService{

    // 자동주입 대상은 final로 설정
    private final ModelMapper modelMapper;
    private final TodoRepository todoRepository;

    // 등록 메서드
    @Override
    // TodoDTO를 Todo 엔티티로 변환하여 데이터베이스에 저장 후, 저장된 Todo 객체의 고유 식별자(tno)를 반환
    public Long register(TodoDTO todoDTO) {
        log.info("-------------");

        // modelMapper를 사용하여 TodoDTO 객체를 Todo 엔티티 객체로 변환
        Todo todo = modelMapper.map(todoDTO, Todo.class);

        // 변환된 'Todo' 엔티티 객체를 todoRepository를 통해 DB에 저장
        Todo savedTodo = todoRepository.save(todo);

        return savedTodo.getTno();
    }

    // 조회 메서드
    // 특정 ID('tno')를 기반으로 "Todo" 엔티티를 조회한 후, 이를 'TodoDTO'로 변환하여 반환
    @Override
    public TodoDTO get(Long tno) {
        // 'tno'에 해당하는 'Todo' 엔티티를 DB에서 조회
        // 'Optional<Todo>'로 반환. -> 조회된 값이 존재하지 않을 경우 안전하게 처리 가능
        java.util.Optional<Todo> result = todoRepository.findById(tno);

        // 조회된 결과가 존재하지 않을 경우 예외를 던짐
        Todo todo = result.orElseThrow();
        TodoDTO dto = modelMapper.map(todo, TodoDTO.class);
        return dto;
    }

    @Override
    public void modify(TodoDTO todoDTO) {
        Optional<Todo> result = todoRepository.findById(todoDTO.getTno());

        // 조회된 id가 없으면 예외 던짐
        Todo todo = result.orElseThrow();

        todo.changeTitle(todoDTO.getTitle());
        todo.changeCompleted(todoDTO.isCompleted());
        todo.changeDueDate(todoDTO.getDueDate());

        todoRepository.save(todo);
    }

    @Override
    public void remove(Long tno) {
        todoRepository.deleteById(tno);
    }

    @Override
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of (
                pageRequestDTO.getPage() - 1, // 1페이지가 0이므로 주의
                pageRequestDTO.getSize(), Sort.by("tno").descending());

        // 페이징 처리된 'Todo' 엔티티 목록을 조회
        Page<Todo> result = todoRepository.findAll(pageable);

        // 조회된 'Todo' 엔티티 목록('result.getContent()')을 스트림으로 변환하여
        // 각각의 'Todo'객체를 'TodoDTO'로 매핑.
        List<TodoDTO> dtoList = result.getContent().stream().map(todo ->
                modelMapper.map(todo, TodoDTO.class)).collect(Collectors.toList());

        // 전체 데이터 개수를 가져옴. 페이징 처리에 사용
        long totalCount = result.getTotalElements();

        // PageResponseDTO 빌더를 사용하여 응답 객체 생성
        PageResponseDTO<TodoDTO> responseDTO = PageResponseDTO.<TodoDTO>withAll().dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO).totalCount(totalCount).build();

        return responseDTO;
    }
}
