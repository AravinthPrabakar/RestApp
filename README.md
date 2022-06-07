# Getting Started

### Reference Documentation
# REST Application API
Provides backend API for the application

**REST Application API**

The project consist of Vert.x Application - Reactive framework -https://vertx.io/ which has below API 
1. **GET** - /api/smaato/accept?id=1001&endpoint=https://google.com
2. Validation of the query parameters,
    - **SUCCESS** - "ok"
    - **FAILED** - "failed"
3. Every GET would persist the id in the redis and increment the unique-request counter. 
   This is enable id deduplication in case service has multiple instances  behind loadbalancer.  
4. A Scheduled thread would log the unique-request count to log file. - **currentdir/app.log**
5. If "endpoint" query parameters is passed in the request, **POST** would be issued and HTTP response code would be logged.

**For Example**

[INFO ] 2022-06-05 20:32:00.170 [vert.x-eventloop-thread-1] App - **10** \
[INFO ] 2022-06-05 20:33:00.169 [vert.x-eventloop-thread-1] App - **5** \
[INFO ] 2022-06-05 20:34:00.168 [vert.x-eventloop-thread-1] App - **17** \
[INFO ] 2022-06-05 20:35:00.172 [vert.x-eventloop-thread-1] App - **12** \
[INFO ] 2022-06-05 20:36:00.172 [vert.x-eventloop-thread-1] App - **Endpoint response: 200** \
[INFO ] 2022-06-05 20:37:00.172 [vert.x-eventloop-thread-1] App - **0** \
[INFO ] 2022-06-05 20:38:00.172 [vert.x-eventloop-thread-1] App - **0** 

For simplicity, User authentication and Authorization has been not
provided. As name suggests, it’s not a UI but backend.


**Prerequisites:**
-   Run Redis docker locally \
    docker run --name my-redis -p 6379:6379 -d redis \
    docker ps
    
-   Install JDK 11


**How to Run:**

cd to path where application is downloaded/cloned.

-   cd RestApp

-   java -jar target/App.jar

