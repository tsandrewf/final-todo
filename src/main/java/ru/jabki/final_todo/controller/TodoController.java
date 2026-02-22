package ru.jabki.final_todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.jabki.final_todo.model.Todo;
import ru.jabki.final_todo.model.TodoResponse;
import ru.jabki.final_todo.model.TodoUpdate;
import ru.jabki.final_todo.service.TodoService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todo")
@Tag(name = "Пользователи")
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
    public List<TodoResponse> list() {
        return todoService.list();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление задачи")
    @ResponseBody
    public Map<String, Boolean> delete(@PathVariable("id") Long id) {
        todoService.delete(id);
        return Collections.singletonMap("success", true);
    }

    @PatchMapping
    @Operation(summary = "Обновление задачи")
    public TodoResponse update(@RequestBody final TodoUpdate todoUpdate) {
        return todoService.update(todoUpdate);
    }
}
