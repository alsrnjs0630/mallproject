package com.lab.mallrestful.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {

    private Long tno;
    private String title;
    private String author;
    private boolean completed;

    // JSON 직렬화/역질렬화 과정에서 날짜 포맷 지정하기 위해 사용
    // 'shape = JsonFormat.Shape.STRING' : 날짜 데이터를 문자열 형태로 변환 JSON으로 직렬화될 때 날짜를 문자열로 표현하기 위해 설정
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
}
