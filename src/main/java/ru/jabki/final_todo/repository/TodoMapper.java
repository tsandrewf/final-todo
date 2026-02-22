package ru.jabki.final_todo.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.jabki.final_todo.model.TodoResponse;
import ru.jabki.final_todo.model.Status;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TodoMapper implements RowMapper<TodoResponse> {

    @Override
    public TodoResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TodoResponse.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .status(Status.getById(rs.getInt("status")))
                .deadLine(rs.getDate("dead_line").toLocalDate())
                .authorId(rs.getLong("author_id"))
                .assigneeId(rs.getLong("assignee_id"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .updatedAt(rs.getTimestamp("updated_at") == null ? null : rs.getTimestamp("updated_at").toLocalDateTime())
                .build();
    }
}
