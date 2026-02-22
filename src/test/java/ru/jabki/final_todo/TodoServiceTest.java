package ru.jabki.final_todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.jabki.final_todo.exception.TodoException;
import ru.jabki.final_todo.model.Todo;
import ru.jabki.final_todo.model.TodoResponse;
import ru.jabki.final_todo.model.Status;
import ru.jabki.final_todo.model.TodoUpdate;
import ru.jabki.final_todo.repository.TodoRepository;
import ru.jabki.final_todo.service.ExternalUserService;
import ru.jabki.final_todo.service.TodoService;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private ExternalUserService externalUserService;

    @InjectMocks
    private TodoService todoService;

    @Test
    void createTodo_valid() {
        final Todo todo = getTodo();
        final TodoResponse todoResponse = getTodoResponse();

        Mockito.when(todoRepository.insert(todo)).thenReturn(todoResponse);
        Mockito.when(externalUserService.isUserExists(todo.getAuthorId())).thenReturn(true);
        Mockito.when(externalUserService.isUserExists(todo.getAssigneeId())).thenReturn(true);

        TodoResponse result = todoService.create(todo);

        assertThat(result).isEqualTo(todoResponse);
        verify(todoRepository).insert(todo);
    }

    @Test
    void createTodo_nullTitle_throwsTodoException() {
        final Todo todo = getTodo();
        todo.setTitle(null);

        final TodoException exception = assertThrows(
                TodoException.class,
                () -> todoService.create(todo)
        );

        assertEquals(exception.getMessage(), "Заголовок задачи не задан");

        verify(todoRepository, never()).insert(any());
    }

    @Test
    void createTodo_nullDescription_throwsTodoException() {
        final Todo todo = getTodo();
        todo.setDescription(null);

        final TodoException exception = assertThrows(
                TodoException.class,
                () -> todoService.create(todo)
        );

        assertEquals(exception.getMessage(), "Описание задачи не задано");

        verify(todoRepository, never()).insert(any());
    }

    @Test
    void createTodo_nullStatus_throwsTodoException() {
        final Todo todo = getTodo();
        todo.setStatus(null);

        final TodoException exception = assertThrows(
                TodoException.class,
                () -> todoService.create(todo)
        );

        assertEquals(exception.getMessage(), "Статус задачи не задан");

        verify(todoRepository, never()).insert(any());
    }

    @Test
    void createTodo_nullDeadLine_throwsTodoException() {
        final Todo todo = getTodo();
        todo.setDeadLine(null);

        final TodoException exception = assertThrows(
                TodoException.class,
                () -> todoService.create(todo)
        );

        assertEquals(exception.getMessage(), "Дата окончания задачи не задана");

        verify(todoRepository, never()).insert(any());
    }

    @Test
    void createTodo_badDeadLine_throwsTodoException() {
        final Todo todo = getTodo();
        final LocalDate deadLine = LocalDate.parse("2026-01-01");
        todo.setDeadLine(deadLine);

        final TodoException exception = assertThrows(
                TodoException.class,
                () -> todoService.create(todo)
        );

        assertEquals(exception.getMessage(), String.format("Дата окончания задачи '%s' должна быть позже текущей", deadLine));

        verify(todoRepository, never()).insert(any());
    }

    @Test
    void createTodo_nullAuthorId_throwsTodoException() {
        final Todo todo = getTodo();
        todo.setAuthorId(null);

        final TodoException exception = assertThrows(
                TodoException.class,
                () -> todoService.create(todo)
        );

        assertEquals(exception.getMessage(), "Автор не задан");

        verify(todoRepository, never()).insert(any());
    }

    @Test
    void createTodo_badAuthorId_throwsTodoException() {
        final Todo todo = getTodo();

        final TodoException exception = assertThrows(
                TodoException.class,
                () -> todoService.create(todo)
        );

        assertEquals(exception.getMessage(), "Автор с id '1' не найден");

        verify(todoRepository, never()).insert(any());
    }

    @Test
    void createTodo_nullAssigneeId_throwsTodoException() {
        final Todo todo = getTodo();
        todo.setAssigneeId(null);

        Mockito.when(externalUserService.isUserExists(todo.getAuthorId())).thenReturn(true);

        final TodoException exception = assertThrows(
                TodoException.class,
                () -> todoService.create(todo)
        );

        assertEquals(exception.getMessage(), "Ответственный за выполнение не задан");

        verify(todoRepository, never()).insert(any());
    }

    @Test
    void createTodo_badAssigneeId_throwsTodoException() {
        final Todo todo = getTodo();

        Mockito.when(externalUserService.isUserExists(todo.getAuthorId())).thenReturn(true);

        final TodoException exception = assertThrows(
                TodoException.class,
                () -> todoService.create(todo)
        );

        assertEquals(exception.getMessage(), "Ответственный за выполнение с id '2' не найден");

        verify(todoRepository, never()).insert(any());
    }

    @Test
    void updateTodo_valid() {
        final TodoUpdate todoUpdate = getTodoUpdate();
        final TodoResponse todoResponse = getTodoResponse();

        Mockito.when(todoRepository.update(todoUpdate)).thenReturn(todoResponse);
        Mockito.when(externalUserService.isUserExists(todoUpdate.getEditorId())).thenReturn(true);

        TodoResponse result = todoService.update(todoUpdate);

        assertThat(result).isEqualTo(todoResponse);
        verify(todoRepository).update(todoUpdate);
    }

    @Test
    void updateTodo_nullTitle_throwsTodoException() {
        final TodoUpdate todoUpdate = getTodoUpdate();
        todoUpdate.setTitle(null);

        final TodoException exception = assertThrows(
                TodoException.class,
                () -> todoService.update(todoUpdate)
        );

        assertEquals(exception.getMessage(), "Заголовок задачи не задан");

        verify(todoRepository, never()).insert(any());
    }

    @Test
    void updateTodo_nullStatus_throwsTodoException() {
        final TodoUpdate todoUpdate = getTodoUpdate();
        todoUpdate.setStatus(null);

        final TodoException exception = assertThrows(
                TodoException.class,
                () -> todoService.update(todoUpdate)
        );

        assertEquals(exception.getMessage(), "Статус задачи не задан");

        verify(todoRepository, never()).insert(any());
    }

    private Todo getTodo() {
        return Todo.builder()
                .title("Test Title")
                .description("Test Description")
                .status(Status.TO_DO)
                .deadLine(LocalDate.parse("2026-06-27"))
                .authorId(1L)
                .assigneeId(2L)
                .build();
    }

    private TodoResponse getTodoResponse() {
        return TodoResponse.builder()
                .id(1L)
                .title("Test Title")
                .description("Test Description")
                .status(Status.TO_DO)
                .deadLine(LocalDate.parse("2026-06-27"))
                .authorId(1L)
                .assigneeId(2L)
                .createdAt(LocalDateTime.parse("2026-02-18T21:09:54.927454"))
                .updatedAt(null)
                .build();
    }

    private TodoUpdate getTodoUpdate() {
        return TodoUpdate.builder()
                .title("Test Title Updated")
                .status(Status.TO_DO)
                .editorId(3L)
                .build();
    }
}
