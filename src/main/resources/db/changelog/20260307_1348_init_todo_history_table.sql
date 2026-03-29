CREATE TABLE final_todo.todo_history (
    id SERIAL PRIMARY KEY,
    todo_id INTEGER NOT NULL,
    title VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    status INTEGER NOT NULL CHECK (status BETWEEN 1 AND 4),
    dead_line DATE NOT NULL,
    author_id INTEGER NOT NULL,
    assignee_id INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL
);

CREATE UNIQUE INDEX todo_history_UQ1 ON final_todo.todo_history
(todo_id, COALESCE(updated_at, created_at));
