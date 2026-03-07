package ru.jabki.final_todo.model;

import lombok.Data;

@Data
public class ApiError {

    final boolean success;
    final String message;
}
