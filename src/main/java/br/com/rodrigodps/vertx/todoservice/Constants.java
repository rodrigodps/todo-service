package br.com.rodrigodps.vertx.todoservice;

public class Constants {

    /**
     * API Route
     */
    public static final String API_GET = "/todos/:todoId";
    public static final String API_LIST_ALL = "/todos";
    public static final String API_CREATE = "/todos";
    public static final String API_UPDATE = "/todos/:todoId";
    public static final String API_DELETE = "/todos/:todoId";
    public static final String API_DELETE_ALL = "/todos";

    /**
     * Redis
     */
    public static final String REDIS_TODO_KEY = "VERT_TODO";
    public static final String REDIS_HOST = "127.0.0.1";
    public static final int REDIS_PORT = 6379;

    /**
     * Http
     */
    public static final String HTTP_HOST = "0.0.0.0";
    public static final int HTTP_PORT = 8082;

    /**
     * Service type
     */
    public static final String SERVICE_TYPE_JDBC = "jdbc";
    public static final String SERVICE_TYPE_REDIS = "redis";

    private Constants() {
    }

}
