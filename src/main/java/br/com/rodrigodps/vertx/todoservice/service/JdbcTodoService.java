package br.com.rodrigodps.vertx.todoservice.service;

import br.com.rodrigodps.vertx.todoservice.entity.Todo;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class JdbcTodoService implements TodoService {

    private static final Logger LOGGER = Logger.getLogger(JdbcTodoService.class.getName());

    private static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS `todo` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `title` varchar(255) DEFAULT NULL,\n" +
            "  `completed` tinyint(1) DEFAULT NULL,\n" +
            "  `order` int(11) DEFAULT NULL,\n" +
            "  `url` varchar(255) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`) )";
    private static final String SQL_INSERT = "INSERT INTO `todo` " +
            "(`id`, `title`, `completed`, `order`, `url`) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_QUERY = "SELECT * FROM todo WHERE id = ?";
    private static final String SQL_QUERY_ALL = "SELECT * FROM todo";
    private static final String SQL_UPDATE = "UPDATE `todo`\n" +
            "SET `id` = ?,\n" +
            "`title` = ?,\n" +
            "`completed` = ?,\n" +
            "`order` = ?,\n" +
            "`url` = ?\n" +
            "WHERE `id` = ?;";
    private static final String SQL_DELETE = "DELETE FROM `todo` WHERE `id` = ?";
    private static final String SQL_DELETE_ALL = "DELETE FROM `todo`";

    private final Vertx vertx;
    private final JsonObject config;
    private final JDBCClient client;

    public JdbcTodoService(Vertx vertx, JsonObject config) {
        this.vertx = vertx;
        this.config = config;
        this.client = JDBCClient.createShared(vertx, config);
    }

    @Override
    public Future<Boolean> initData() {
        Future<Boolean> result = Future.future();
        client.getConnection(connHandler(result, connection ->
                connection.execute(SQL_CREATE, create -> {
                    if (create.succeeded()) {
                        result.complete(true);
                    } else {
                        result.fail(create.cause());
                    }
                    connection.close();
                })));
        return result;
    }

    @Override
    public Future<Boolean> insert(Todo todo) {
        Future<Boolean> result = Future.future();
        client.getConnection(connHandler(result,
                connection -> {
                    connection.updateWithParams(SQL_INSERT, new JsonArray()
                            .add(todo.getId())
                            .add(todo.getTitle())
                            .add(todo.isCompleted())
                            .add(todo.getOrder())
                            .add(todo.getUrl()), r -> {
                        if (r.failed()) {
                            result.fail(r.cause());
                        } else {
                            result.complete(true);
                        }
                        connection.close();
                    });
                }));
        return result;
    }

    @Override
    public Future<List<Todo>> getAll() {
        Future<List<Todo>> result = Future.future();
        client.getConnection(connHandler(result, connection -> {
            connection.query(SQL_QUERY, r -> {
                if (r.failed()) {
                    result.fail(r.cause());
                } else {
                    List<Todo> list = new ArrayList<>();
                    r.result().getRows().forEach(x -> {
                        list.add(new Todo(x));
                    });
                    result.complete(list);
                }
                connection.close();
            });
        }));
        return result;
    }

    @Override
    public Future<Optional<Todo>> getCertain(String todoID) {
        Future<Optional<Todo>> result = Future.future();
        client.getConnection(connHandler(result, connection -> {
            connection.queryWithParams(SQL_QUERY, new JsonArray().add(todoID), r -> {
                if (r.failed()) {
                    result.fail(r.cause());
                } else {
                    List<JsonObject> list = r.result().getRows();
                    if (list == null || list.isEmpty()) {
                        result.complete(Optional.empty());
                    } else {
                        result.complete(Optional.of(new Todo(list.get(0))));
                    }
                }
                connection.close();
            });
        }));
        return result;
    }

    @Override
    public Future<Todo> update(String todoId, Todo newTodo) {
        Future<Todo> result = Future.future();
        client.getConnection(connHandler(result,
                connection -> {
                    connection.updateWithParams(SQL_UPDATE, new JsonArray()
                            .add(newTodo.getId())
                            .add(newTodo.getTitle())
                            .add(newTodo.isCompleted())
                            .add(newTodo.getOrder())
                            .add(newTodo.getUrl())
                            .add(newTodo.getId()), r -> {
                        if (r.failed()) {
                            result.fail(r.cause());
                        } else {
                            result.complete(newTodo);
                        }
                        connection.close();
                    });
                }));
        return result;
    }

    @Override
    public Future<Boolean> delete(String todoId) {
        Future<Boolean> result = Future.future();
        client.getConnection(connHandler(result, connection -> {
            connection.updateWithParams(SQL_DELETE, new JsonArray().add(todoId), r -> {
                if (r.failed()) {
                    result.fail(r.cause());
                } else {
                    result.complete(true);
                }
                connection.close();
            });
        }));
        return result;
    }

    @Override
    public Future<Boolean> deleteAll() {
        Future<Boolean> result = Future.future();
        client.getConnection(connHandler(result, connection -> {
            connection.update(SQL_DELETE_ALL, r -> {
                if (r.failed()) {
                    result.fail(r.cause());
                } else {
                    result.complete(true);
                }
                connection.close();
            });
        }));
        return result;
    }

    private Handler<AsyncResult<SQLConnection>> connHandler(Future future, Handler<SQLConnection> handler) {
        return conn -> {
            if (conn.succeeded()) {
                final SQLConnection connection = conn.result();
                handler.handle(connection);
            } else {
                future.fail(conn.cause());
            }
        };
    }

}
