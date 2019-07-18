package br.com.rodrigodps.vertx.todoservice.service;

import br.com.rodrigodps.vertx.todoservice.entity.Todo;
import io.vertx.core.Future;

import java.util.List;
import java.util.Optional;

public interface TodoService {

    Future<Boolean> initData(); // init the data (or table)

    Future<Boolean> insert(Todo todo);

    Future<List<Todo>> getAll();

    Future<Optional<Todo>> getCertain(String todoID);

    Future<Todo> update(String todoId, Todo newTodo);

    Future<Boolean> delete(String todoId);

    Future<Boolean> deleteAll();

}
