package io.vertx.smaato.web.rest.scheduler;

import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;

import io.vertx.smaato.web.rest.App;
import io.vertx.smaato.web.rest.util.AppHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
                     redis.getset(AppHelper.uniqueReq,"0").onSuccess(result->LOGGER.info(String.valueOf(result)) );
                }catch(Exception e){
                    LOGGER.error("FAILED",e);
                }
            }, 0,60,
                TimeUnit.SECONDS);

    }

}
