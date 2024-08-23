package com.lab.mallrestful.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_todo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;
    private String title;
    private String author;
    private Boolean completed;
    private LocalDate dueDate;

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeCompleted(Boolean completed) {
        this.completed = completed;
    }

    public void changeDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
