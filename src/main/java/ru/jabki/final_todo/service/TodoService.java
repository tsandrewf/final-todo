package ru.jabki.final_todo.service;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.jabki.final_todo.exception.TodoException;
import ru.jabki.final_todo.model.Status;
import ru.jabki.final_todo.model.Todo;
import ru.jabki.final_todo.model.TodoResponse;
import ru.jabki.final_todo.model.TodoUpdate;
import ru.jabki.final_todo.repository.TodoHistoryRepository;
import ru.jabki.final_todo.repository.TodoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoHistoryRepository todoHistoryRepository;
    private ExternalUserService externalUserService;

    @Secured("ROLE_MANAGER")
    @Transactional(rollbackFor = Exception.class)
    public TodoResponse create(final Todo todo) {
        validate(todo);
        return todoRepository.insert(todo);
    }

    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    @Transactional(readOnly = true)
    public TodoResponse getById(final long id) {
        return todoRepository.getById(id);
    }

    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    @Transactional(readOnly = true)
    public List<TodoResponse> list(Status status, Long assigneeId) {
        return todoRepository.list(status, assigneeId);
    }

    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    @Transactional(rollbackFor = Exception.class)
    public TodoResponse update(final TodoUpdate todoUpdate) {
        TodoResponse todoResponseOld = todoRepository.getById(todoUpdate.getId());
        validateUpdate(todoUpdate, todoResponseOld);
        TodoResponse todoResponse = todoRepository.update(todoUpdate);
        todoHistoryRepository.insertTodoHistory(todoResponseOld);
        return todoResponse;
    }

    @Secured({"ROLE_MANAGER", "ROLE_USER"})
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

    private void validateUpdate(final TodoUpdate todoUpdate, TodoResponse todoResponseOld) {
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

        Status statusOld = todoResponseOld.getStatus();

        if (todoUpdate.getTitle().equals(todoResponseOld.getTitle())
                && todoUpdate.getDescription().equals(todoResponseOld.getDescription())
                && todoUpdate.getDeadLine().equals(todoResponseOld.getDeadLine())
                && todoUpdate.getAssigneeId().equals(todoResponseOld.getAssigneeId())
                && todoUpdate.getStatus().equals(statusOld)
        ) {
            throw new TodoException("В задаче ничего не изменилось");
        }

        Status statusNew = todoUpdate.getStatus();

        if ((statusOld == Status.TO_DO) && (statusNew == Status.DONE)) {
            throw new TodoException("Задача не может быть сразу завершена. Она должна пройти через IN_PROGRESS");
        }
        if ((statusOld == Status.IN_PROGRESS) && (statusNew == Status.TO_DO)) {
            throw new TodoException("Задача не может вернуться в исходное состояние после того, как она начала выполняться");
        }
        if ((statusOld == Status.DONE) && (statusNew == Status.IN_PROGRESS)) {
            throw new TodoException("Задача не может вернуться в состояние работы, если она уже была завершена. Её можно только удалить или оставить в статусе завершённой");
        }
        if ((statusOld == Status.DELETE) && (statusNew != Status.DELETE)) {
            throw new TodoException("Статус DELETE — финальный статус. После этого задача не может быть активной и не может вернуться в любой другой статус");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!todoUpdate.getAssigneeId().equals(todoResponseOld.getAssigneeId())
                && (auth == null
                    || auth.getAuthorities().stream().noneMatch(a -> Objects.equals(a.getAuthority(), "ROLE_MANAGER")))) {
            throw new TodoException("Только MANAGER может менять исполнителя у задачи");
        }
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
