package ru.jabki.final_todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jabki.final_todo.model.TodoResponse;
import ru.jabki.final_todo.service.TodoHistoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todo_history")
@Tag(name = "История событий задач")
public class TodoHistoryController {

    private final TodoHistoryService todoHistoryService;

    @GetMapping("/{id}")
    @Operation(summary = "История изменений задачи")
    public List<TodoResponse> getByIdHistory(@PathVariable("id") Long id) {
        return todoHistoryService.getByIdHistory(id);
    }
}
