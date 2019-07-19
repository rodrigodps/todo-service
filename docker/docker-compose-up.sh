mkdir -p libs
mkdir -p config
cp ../build/libs/vertx-todo-service-fat.jar libs
cp ../config/* config
docker-compose up
