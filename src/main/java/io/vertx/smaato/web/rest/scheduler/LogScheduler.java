package io.vertx.smaato.web.rest.scheduler;

import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;

import io.vertx.redis.client.Response;
import io.vertx.redis.client.ResponseType;
import io.vertx.smaato.web.rest.App;
import io.vertx.smaato.web.rest.util.AppHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LogScheduler {
    Redis client = null;
    RedisAPI redis = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    public LogScheduler(Redis client){
        this.client = client;
        this.redis = RedisAPI.api(client);
    }

    public void start(){
        ScheduledExecutorService scheduler
                = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(()->{
                try{
                    redis.get(AppHelper.uniqueReq).onSuccess(result->LOGGER.info(result==null? "0": String.valueOf(result)) ).onComplete(result ->{
                        redis.keys("*").onSuccess(keys-> {
                            if (keys.type() == ResponseType.MULTI) {
                                List<String> aList = new ArrayList<>();
                                aList.addAll(keys.stream().map(e->e.toString()).collect(Collectors.toList()));
                                redis.del(aList);
                            }
                        }).onFailure(ex->ex.printStackTrace());
                        redis.set(Arrays.asList(AppHelper.uniqueReq,"0"));
                    });
                }catch(Exception e){
                    LOGGER.error("FAILED",e);
                }
            }, 0,60,
                TimeUnit.SECONDS);

    }

}
