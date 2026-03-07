package ru.jabki.final_todo.exception;

public class TodoByIdNotFoundException extends RuntimeException {

    public TodoByIdNotFoundException(final long id) {
        super(String.format("Задача с id %s не найдена", id));
    }
}
