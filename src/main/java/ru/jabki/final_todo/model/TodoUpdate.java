package ru.jabki.final_todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoUpdate {

    private Long id;
    private String title;
    private String description;
    private LocalDate deadLine;
    private Long assigneeId;
    private Status status;
    private Long editorId;
}
