package ru.jabki.final_todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.jabki.final_todo.exception.TodoException;
import ru.jabki.final_todo.model.Status;
import ru.jabki.final_todo.model.Todo;
import ru.jabki.final_todo.model.TodoResponse;
import ru.jabki.final_todo.model.TodoUpdate;
import ru.jabki.final_todo.repository.TodoRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private ExternalUserService externalUserService;

    @Transactional(rollbackFor = Exception.class)
    public TodoResponse create(final Todo todo) {
        validate(todo);
        return todoRepository.insert(todo);
    }

    @Transactional(readOnly = true)
    public TodoResponse getById(final long id) {
        return todoRepository.getById(id);
    }

    @Transactional(readOnly = true)
    public List<TodoResponse> list(Status status, Long assigneeId) {
        return todoRepository.list(status, assigneeId);
    }

    @Transactional(rollbackFor = Exception.class)
    public TodoResponse update(final TodoUpdate todoUpdate) {
        validateUpdate(todoUpdate);
        return todoRepository.update(todoUpdate);
    }

    @Transactional(readOnly = true)
    public boolean userByIdInvolved(final long userId) {
        return todoRepository.userByIdInvolved(userId);
    }

    private void validate(final Todo todo) {
        if (todo == null) {
            throw new TodoException("Задача не задана");
        }
        if (!StringUtils.hasText(todo.getTitle())) {
            throw new TodoException("Заголовок задачи не задан");
        }
        if (!StringUtils.hasText(todo.getDescription())) {
            throw new TodoException("Описание задачи не задано");
        }
        if (todo.getStatus() == null) {
            todo.setStatus(Status.TO_DO);
        }
        if (todo.getDeadLine() == null) {
            throw new TodoException("Дата окончания задачи не задана");
        }
        if (todo.getDeadLine().isBefore(LocalDate.now())) {
            throw new TodoException(String.format("Дата окончания задачи '%s' должна быть позже текущей", todo.getDeadLine()));
        }

        validateUser(todo.getAuthorId(), "Автор");
        validateUser(todo.getAssigneeId(), "Ответственный за выполнение");
    }

    private void validateUpdate(final TodoUpdate todoUpdate) {
        if (todoUpdate == null) {
            throw new TodoException("Задача не задана");
        }
        if (!StringUtils.hasText(todoUpdate.getTitle())) {
            throw new TodoException("Заголовок задачи не задан");
        }
        if (todoUpdate.getStatus() == null) {
            throw new TodoException("Статус задачи не задан");
        }

        validateUser(todoUpdate.getAssigneeId(), "Ответственный за выполнение");
        validateUser(todoUpdate.getEditorId(), "Редактор");
    }

    private void validateUser(final Long id, final String title) {
        if (id == null) {
            throw new TodoException(String.format("%s не задан", title));
        }
        if (!externalUserService.isUserExists(id)) {
            throw new TodoException(String.format("%s с id '%s' не найден", title, id));
        }
    }
}
