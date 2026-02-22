package ru.jabki.final_todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class TodoResponse {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private LocalDate deadLine;
    private Long authorId;
    private Long assigneeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
