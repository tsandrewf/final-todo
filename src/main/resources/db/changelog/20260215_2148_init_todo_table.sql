CREATE SCHEMA final_todo;

CREATE TABLE final_todo.todo (
    id SERIAL PRIMARY KEY,
    title VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    status INTEGER NOT NULL CHECK (status BETWEEN 1 AND 4),
    dead_line DATE NOT NULL,
    author_id INTEGER NOT NULL,
    assignee_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL
);
