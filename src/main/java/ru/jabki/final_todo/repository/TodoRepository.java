package ru.jabki.final_todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jabki.final_todo.exception.BadRequestException;
import ru.jabki.final_todo.exception.TodoByIdNotFoundException;
import ru.jabki.final_todo.model.Status;
import ru.jabki.final_todo.model.Todo;
import ru.jabki.final_todo.model.TodoResponse;
import ru.jabki.final_todo.model.TodoUpdate;

import java.util.List;

@Repository
@AllArgsConstructor
public class TodoRepository {

    private static final String INSERT = """
            INSERT INTO final_todo.todo (title, description, status, dead_line, author_id, assignee_id, created_at)
            VALUES (:title, :description, :status, :dead_line, :author_id, :assignee_id, now())
            RETURNING *;
            """;

    private static final String GET_BY_ID = """
            SELECT *
            FROM final_todo.todo
            WHERE id = :id
            AND status <> 4
            """;


    private static final String LIST = """
            SELECT *
            FROM final_todo.todo
            WHERE status <> 4
            """;

    private static final String UPDATE = """
            UPDATE final_todo.todo
            SET title = :title, description =:description, dead_line =:dead_line, assignee_id = :assignee_id, status = :status, updated_at = now()
            WHERE id = :id
            RETURNING *;
            """;

    private static String getSearchSql(Status status, Long assigneeId) {
        String searchSql = """
            SELECT *
            FROM final_todo.todo
            WHERE status <> 4
                """;

        if (status != null) {
            searchSql = searchSql.concat(" AND status = :status");
        }

        if (assigneeId != null) {
            searchSql = searchSql.concat(" AND assignee_id = :assignee_id");
        }

        return searchSql;
    }

    private static final String USER_BY_ID_INVOLVED = """
            SELECT EXISTS (
                SELECT 1
                FROM final_todo.todo
                WHERE (author_id = :user_id OR assignee_id = :user_id)
                AND status <> 4
            )
            """;

    private final TodoMapper todoMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TodoResponse insert(final Todo todo) {
        return jdbcTemplate.queryForObject(INSERT, todoToSql(todo), todoMapper);
    }

    public TodoResponse getById(final Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, new MapSqlParameterSource("id", id), todoMapper);
        } catch (DataAccessException e) {
            throw new TodoByIdNotFoundException(id);
        }
    }

    public List<TodoResponse> list(Status status, Long assigneeId) {
        return jdbcTemplate.query(getSearchSql(status, assigneeId), searchToSql(status, assigneeId), todoMapper);
    }

    public TodoResponse update(final TodoUpdate todoUpdate) {
        return jdbcTemplate.queryForObject(UPDATE, todoUpdateToSql(todoUpdate), todoMapper);
    }

    public boolean userByIdInvolved(final Long userId) {
        /*try {
            jdbcTemplate.queryForObject(USER_BY_ID_INVOLVED, new MapSqlParameterSource("user_id", userId), todoMapper);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }*/
        return Boolean.TRUE.equals(
                jdbcTemplate.queryForObject(USER_BY_ID_INVOLVED, new MapSqlParameterSource("user_id", userId), Boolean.class));
    }

    public MapSqlParameterSource todoToSql(final Todo todo) {
        final MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("title", todo.getTitle());
        params.addValue("description", todo.getDescription());
        params.addValue("status", todo.getStatus().getId());
        params.addValue("dead_line", todo.getDeadLine());
        params.addValue("author_id", todo.getAuthorId());
        params.addValue("assignee_id", todo.getAssigneeId());

        return params;
    }

    public MapSqlParameterSource todoUpdateToSql(final TodoUpdate todoUpdate) {
        final MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("id", todoUpdate.getId());
        params.addValue("title", todoUpdate.getTitle());
        params.addValue("description", todoUpdate.getDescription());
        params.addValue("dead_line", todoUpdate.getDeadLine());
        params.addValue("assignee_id", todoUpdate.getAssigneeId());
        params.addValue("status", todoUpdate.getStatus().getId());

        return params;
    }

    public MapSqlParameterSource searchToSql(Status status, Long assigneeId) {
        final MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("status", (status == null ? null : status.getId()));
        params.addValue("assignee_id", assigneeId);

        return params;
    }
}
