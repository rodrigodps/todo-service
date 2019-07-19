package br.com.rodrigodps.vertx.todoservice.service;

import br.com.rodrigodps.vertx.todoservice.Constants;
import br.com.rodrigodps.vertx.todoservice.entity.Todo;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RedisTodoService implements TodoService {

    private static final Logger LOGGER = Logger.getLogger(RedisTodoService.class.getName());

    private Vertx vertx;
    private RedisOptions config;
    private RedisClient redis;

    public RedisTodoService(Vertx vertx, RedisOptions config) {
        this.vertx = vertx;
        this.config = config;
    }

    @Override
    public Future<Boolean> initData() {
        this.redis = RedisClient.create(vertx, config);
        return Future.succeededFuture(true);
    }

    @Override
    public Future<Boolean> insert(Todo todo) {
        Future<Boolean> result = Future.future();
        try {
            final String encoded = Json.encodePrettily(todo);
            redis.hset(Constants.REDIS_TODO_KEY, String.valueOf(todo.getId()), encoded, res -> {
                if (res.succeeded())
                    result.complete(true);
                else
                    result.fail(res.cause());
            });
        } catch (DecodeException e) {
            result.fail(e);
        }
        return result;
    }

    @Override
    public Future<List<Todo>> getAll() {
        Future<List<Todo>> result = Future.future();
        redis.hvals(Constants.REDIS_TODO_KEY, res -> {
            if (res.succeeded()) {
                result.complete(res.result().stream().map(x -> new Todo((String) x)).collect(Collectors.toList()));
            } else
                result.fail(res.cause());
        });
        return result;
    }

    @Override
    public Future<Optional<Todo>> getCertain(String todoId) {
        Future<Optional<Todo>> result = Future.future();
        if (todoId == null)
            result.fail("Param \"todoId\" is required!");
        else {
            redis.hget(Constants.REDIS_TODO_KEY, todoId, x -> {
                if (x.succeeded()) {
                    if (result == null)
                        result.complete();
                    else {
                        result.complete(Optional.of(new Todo(x.result())));
                    }
                } else
                    result.fail(x.cause());
            });
        }
        return result;
    }

    @Override
    public Future<Todo> update(String todoId, Todo newTodo) {
        return this.getCertain(todoId).compose(old -> {
            if (old.isPresent()) {
                Todo fnTodo = old.get().merge(newTodo);
                return this.insert(fnTodo).map(r -> r ? fnTodo : null);
            } else {
                return Future.succeededFuture();
            }
        });
    }

    @Override
    public Future<Boolean> delete(String todoId) {
        Future<Boolean> result = Future.future();
        redis.hdel(Constants.REDIS_TODO_KEY, todoId, res -> {
            if (res.succeeded())
                result.complete(true);
            else
                result.fail(res.cause());
        });
        return result;
    }

    @Override
    public Future<Boolean> deleteAll() {
        Future<Boolean> result = Future.future();
        redis.del(Constants.REDIS_TODO_KEY, res -> {
            if (res.succeeded())
                result.complete(true);
            else
                result.fail(res.cause());
        });
        return result;
    }

}
