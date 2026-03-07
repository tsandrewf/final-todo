package ru.jabki.final_todo.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Status {
    TO_DO(1),
    IN_PROGRESS(2),
    DONE(3),
    DELETE(4);

    private final int id;

    Status(int id) {
        this.id = id;
    }

    // https://stackoverflow.com/questions/27484353/gettin-enum-types-may-not-be-instantiated-exception
    final static Map<Integer, Status> map = new HashMap<>();

    static {
        for (Status status : Status.values()) {
            map.put(status.id, status);
        }
    }

    public static Status getById(int id) {
        return map.get(id);
    }
}
