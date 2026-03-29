package ru.jabki.final_todo.service;

import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jabki.final_todo.model.TodoResponse;
import ru.jabki.final_todo.repository.TodoHistoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TodoHistoryService {

    private final TodoHistoryRepository todoHistoryRepository;

    @Secured({"ROLE_MANAGER", "ROLE_USER"})
    @Transactional(readOnly = true)
    public List<TodoResponse> getByIdHistory(final long id) {
        return todoHistoryRepository.getByIdHistory(id);
    }
}
