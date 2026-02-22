package ru.jabki.final_todo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodoUpdate {

    private Long id;
    private String title;
    private Status status;
    private Long editorId;
}
