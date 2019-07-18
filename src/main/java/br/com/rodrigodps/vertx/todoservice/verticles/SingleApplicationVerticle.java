package br.com.rodrigodps.vertx.todoservice.verticles;

import br.com.rodrigodps.vertx.todoservice.Constants;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.redis.RedisClient;

import java.util.HashSet;
import java.util.Set;

public class SingleApplicationVerticle extends AbstractVerticle {

    private static final String HTTP_HOST = "0.0.0.0";
    private static final String REDIS_HOST = "127.0.0.1";
    private static final int HTTP_PORT = 8082;
    private static final int REDIS_PORT = 6379;

    private RedisClient redis;

    @Override
    public void start(Future<Void> future) throws Exception {
//        initData();

        Router router = Router.router(vertx);
        // CORS support
        Set<String> allowHeaders = new HashSet<>();
        allowHeaders.add("x-requested-with");
        allowHeaders.add("Access-Control-Allow-Origin");
        allowHeaders.add("origin");
        allowHeaders.add("Content-Type");
        allowHeaders.add("accept");
        Set<HttpMethod> allowMethods = new HashSet<>();
        allowMethods.add(HttpMethod.GET);
        allowMethods.add(HttpMethod.POST);
        allowMethods.add(HttpMethod.DELETE);
        allowMethods.add(HttpMethod.PATCH);

        router.route().handler(CorsHandler.create("*").allowedHeaders(allowHeaders).allowedMethods(allowMethods));
        router.route().handler(BodyHandler.create());


        // routes
        router.get(Constants.API_GET).handler(this::handleGetTodo);
        router.get(Constants.API_LIST_ALL).handler(this::handleGetAll);
        router.post(Constants.API_CREATE).handler(this::handleCreateTodo);
        router.patch(Constants.API_UPDATE).handler(this::handleUpdateTodo);
        router.delete(Constants.API_DELETE).handler(this::handleDeleteOne);
        router.delete(Constants.API_DELETE_ALL).handler(this::handleDeleteAll);

        vertx.createHttpServer().requestHandler(router::accept).listen(HTTP_PORT, HTTP_HOST, result -> {
            if (result.succeeded())
                future.complete();
            else
                future.fail(result.cause());
        });
    }

    private void handleDeleteAll(RoutingContext routingContext) {

    }

    private void handleDeleteOne(RoutingContext routingContext) {

    }

    private void handleUpdateTodo(RoutingContext routingContext) {

    }

    private void handleCreateTodo(RoutingContext routingContext) {

    }

    private void handleGetAll(RoutingContext routingContext) {

    }

    private void handleGetTodo(RoutingContext routingContext) {

    }

}