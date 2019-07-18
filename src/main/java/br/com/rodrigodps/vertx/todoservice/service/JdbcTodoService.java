package br.com.rodrigodps.vertx.todoservice.service;

import br.com.rodrigodps.vertx.todoservice.entity.Todo;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Optional;

public class JdbcTodoService implements TodoService {

    public JdbcTodoService(Vertx vertx, JsonObject config) {
    }

    @Override
    public Future<Boolean> initData() {
        return null;
    }

    @Override
    public Future<Boolean> insert(Todo todo) {
        return null;
    }

    @Override
    public Future<List<Todo>> getAll() {
        return null;
    }

    @Override
    public Future<Optional<Todo>> getCertain(String todoID) {
        return null;
    }

    @Override
    public Future<Todo> update(String todoId, Todo newTodo) {
        return null;
    }

    @Override
    public Future<Boolean> delete(String todoId) {
        return null;
    }

    @Override
    public Future<Boolean> deleteAll() {
        return null;
    }
}
