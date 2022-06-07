package io.vertx.smaato.web.rest.handler;

import io.vertx.core.*;
import io.vertx.ext.web.RoutingContext;

import java.util.concurrent.CompletableFuture;


public abstract class SyncHandler implements Handler<RoutingContext> {
    @Override
    public void handle(RoutingContext context) {
        runAsync(() -> handleSync(context))
                .exceptionally(throwable -> {
                    context.fail(throwable);
                    return null;
                });
    }

    public static CompletableFuture<Void> runAsync( Runnable runnable) {

        Context context = Vertx.currentContext();
        CompletableFuture<Void> result = new CompletableFuture<>();
        context.owner().executeBlocking((Promise<Void> blocking) -> {
                    try {
                        runnable.run();
                        blocking.complete(null);
                    } catch (Throwable t) {
                        blocking.fail(t);
                    }
                }, false,
                (AsyncResult<Void> asyncResult) -> {
                    if (asyncResult.failed()) {
                        result.completeExceptionally(asyncResult.cause());
                    } else {
                        result.complete(asyncResult.result());
                    }

                });
        return result;
    }

    public abstract void handleSync(RoutingContext context);
}