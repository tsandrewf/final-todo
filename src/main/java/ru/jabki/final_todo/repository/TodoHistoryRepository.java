package ru.jabki.final_todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jabki.final_todo.model.TodoResponse;

import java.util.List;

@Repository
@AllArgsConstructor
public class TodoHistoryRepository {

    private static final String INSERT_TODO_HISTORY = """
            INSERT INTO final_todo.todo_history (todo_id, title, description, status, dead_line, author_id, assignee_id, created_at, updated_at)
            VALUES (:todo_id, :title, :description, :status, :dead_line, :author_id, :assignee_id, :created_at, :updated_at);
            """;

    private static final String GET_BY_ID_HISTORY = """
            SELECT todo_id as id, title, description, status, dead_line, author_id, assignee_id, created_at, updated_at
            FROM final_todo.todo_history
            WHERE todo_id = :id
            ORDER BY COALESCE(updated_at, created_at) DESC
            """;

    private final TodoMapper todoMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final TodoRepository todoRepository;

    public void insertTodoHistory(final TodoResponse todoResponse) {
        jdbcTemplate.update(INSERT_TODO_HISTORY, todoRepository.todoResponseToSql(todoResponse));
    }

    public List<TodoResponse> getByIdHistory(final Long id) {
        return jdbcTemplate.query(GET_BY_ID_HISTORY, new MapSqlParameterSource("id", id), todoMapper);
    }
}
