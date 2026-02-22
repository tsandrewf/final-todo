package ru.jabki.final_todo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.jabki.final_todo.model.ApiError;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUserError(final Exception exception) {
        return ResponseEntity.badRequest()
                .body(
                        new ApiError(
                                false,
                                exception.getMessage()
                        )
                );
    }
}
