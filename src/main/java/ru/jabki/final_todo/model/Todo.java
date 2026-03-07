package ru.jabki.final_todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class Todo {

    private String title;
    private String description;
    private Status status;
    private LocalDate deadLine;
    private Long authorId;
    private Long assigneeId;
}
