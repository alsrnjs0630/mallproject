package com.lab.mallrestful.repository;

import com.lab.mallrestful.domain.Todo;
import com.lab.mallrestful.dto.PageRequestDTO;
import com.lab.mallrestful.dto.PageResponseDTO;
import com.lab.mallrestful.dto.TodoDTO;
import com.lab.mallrestful.service.TodoService;
import com.lab.mallrestful.service.TodoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class TodoRepositoryTest {

    @Autowired
    TodoRepository repository;
    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private TodoServiceImpl todoServiceImpl;
    @Autowired
    private TodoService todoService;

    @Test
    public void testpaging() {
        // Pageable 타입을 사용해서 별도의 코드 작성 없이 페이징 처리.
        // Pageable 객체를 생성하고 페이지 번호, 페이지 크기, 정렬 방식 설정
        // 첫 번째 페이지(0번 페이지) 요청, 한 페이지에 10개의 항목, tno 컬럼을 기준으로 DESC 정렬
        Pageable pageable = PageRequest.of(0, 10, Sort.by("tno").descending());
        // todoRepository에서 findAll 메서드를 사용하여 페이징된 결과를 가져옴.
        Page<Todo> result = todoRepository.findAll(pageable);
        // 조회된 전체 데이터의 개수를 로그로 출력
        log.info(String.valueOf(result.getTotalElements()));
        // 현재 페이지에 해당하는 데이터 목록을 순회하며 각 Todo 객체를 로그로 출력
        result.getContent().stream().forEach(todo -> log.info(String.valueOf(todo)));
    }

    @Test
    public void testList() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1)
                .size(10).build();

        PageResponseDTO<TodoDTO> response = todoService.list(pageRequestDTO);
        log.info(String.valueOf(response));
    }

    @Test
    void create(){

        for (int i=1; i<100; i++){

            repository.save(Todo.builder().title("title"+String.valueOf(i)).author("author"+String.valueOf(i)).dueDate(LocalDate.of(2023,12,21)).build());

        }

    }

    @Test
    void read(){

       Long tno = 99L;

       Optional<Todo> todo =  repository.findById(tno);

        todo.ifPresent(e->{
            log.info("--------------------------->읽은 Todo{}", e );
        });
    }


    @Test
    void update(){


        Long tno = 99L;

        Optional<Todo> todo =  repository.findById(tno);

        todo.ifPresent(e->{

            e.setTitle("updated title");
            e.setAuthor("updated author");
            e.setDueDate(LocalDate.of(2023,12,31));

            Todo updateDodo = repository.save(e);

            log.info("--------------------------->업데이트 Todo{}", updateDodo );
        });
    }

    @Test
    void delete(){

        Long tno = 99L;
        Optional<Todo> todo =  repository.findById(tno);

        todo.ifPresent(e->{
            repository.deleteById(e.getTno());
            log.info("삭제완료");
        });

    }

    @Test
    void findAll(){

        List<Todo> todoList = repository.findAll();

        todoList.forEach(entity->{

            log.info("-----------> 읽기:{}", entity.toString());

        });
    }
}