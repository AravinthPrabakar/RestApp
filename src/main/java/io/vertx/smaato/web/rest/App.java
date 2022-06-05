package io.vertx.smaato.web.rest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisOptions;
import io.vertx.smaato.web.rest.handler.GetHandler;
import io.vertx.smaato.web.rest.scheduler.LogScheduler;
import io.vertx.smaato.web.rest.util.Runner;

public class App extends AbstractVerticle {

    public App() {
    }

    public static void main(String[] args) {
        Runner.runExample(App.class);
    }

    private GetHandler getHandler;

    @Override
    public void start() {

        setUpInitialData();

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.get("/api/smaato/accept/").handler(getHandler);

        vertx.createHttpServer().requestHandler(router).listen(8080);
    }


    private void setUpInitialData() {
        String host = Vertx.currentContext().config().getString("host");
        if (host == null) {
            host = "redis://localhost:6379";
        }

        Redis client = Redis.createClient(Vertx.vertx(), new RedisOptions().addConnectionString(host));

        this.getHandler = new GetHandler( client);
        new LogScheduler(client).start();
    }


}
