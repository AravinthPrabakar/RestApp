package io.vertx.smaato.web.rest.handler;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import io.vertx.ext.web.client.WebClient;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.smaato.web.rest.App;
import io.vertx.smaato.web.rest.util.AppHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class GetHandler extends SyncHandler {

    Redis client = null;
    RedisAPI redis = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public GetHandler(Redis client){
        this.client = client;
        this.redis = RedisAPI.api(client);
    }

    @Override
    public void handleSync(RoutingContext context) {
        final HttpServerRequest request = context.request();
        final HttpServerResponse response = context.response();
        try {
            Integer id = Integer.valueOf(request.getParam("id"));
            String endPoint = request.getParam("endpoint");
            if(endPoint!=null && !endPoint.isEmpty()){
                //This would do validation of the endpoint for the URL format
                URI uri = new URI(endPoint);
            }
            client.connect()
                    .compose(conn -> {
                        return redis.get(String.valueOf(id)).compose(result->{
                            if(result == null || result.toString().isEmpty()){
                                redis.set(Arrays.asList(String.valueOf(id),"1"));
                                redis.incr(AppHelper.uniqueReq);
                            }
                            return redis.get(String.valueOf(id));
                        });
                    }).onFailure(ex->ex.printStackTrace());

            if(endPoint!=null && !endPoint.isEmpty()){
                WebClient client = WebClient.create(Vertx.vertx());
                client.postAbs(endPoint)
                        .sendJsonObject(new JsonObject().put("count",  redis.get(AppHelper.uniqueReq).onSuccess(result->{result.toString();
                        }))).onSuccess(resp->{
                    LOGGER.info("Endpoint response: "+resp.statusCode());
                }).onFailure(resp->{
                    LOGGER.info("Endpoint response: "+resp);
                });
            }


            response.setStatusCode(200).end("ok");
        } catch (Exception e) {
            response.setStatusCode(400).end("failed");
        }


    }
}
