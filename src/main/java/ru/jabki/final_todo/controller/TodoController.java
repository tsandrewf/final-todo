package ru.jabki.final_todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.jabki.final_todo.model.Status;
import ru.jabki.final_todo.model.Todo;
import ru.jabki.final_todo.model.TodoResponse;
import ru.jabki.final_todo.model.TodoUpdate;
import ru.jabki.final_todo.service.TodoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todo")
@Tag(name = "Задачи")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    @Operation(summary = "Создать задачу")
    public TodoResponse create(@RequestBody final Todo todo) {
        return todoService.create(todo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по id")
    public TodoResponse getById(@PathVariable("id") Long id) {
        return todoService.getById(id);
    }

    @GetMapping("/list")
    @Operation(summary = "Получение списка задач")
    public List<TodoResponse> list(@RequestParam(required = false) Status status, @RequestParam(required = false) Long assigneeId) {
        return todoService.list(status, assigneeId);
    }

    @PatchMapping
    @Operation(summary = "Обновление задачи")
    public TodoResponse update(@RequestBody final TodoUpdate todoUpdate) {
        return todoService.update(todoUpdate);
    }

    @GetMapping("/userByIdInvolved/{id}")
    @Operation(summary = "Пользователь с id задействован в задачах")
    public boolean userByIdInvolved(@PathVariable("id") Long userId) {
        return todoService.userByIdInvolved(userId);
    }
}
