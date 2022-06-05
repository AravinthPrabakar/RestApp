# Getting Started

### Reference Documentation
# Survey Application API
Provides backend API for survey application

**REST Application API**

The project consist of Application which has API 
1. GET - /api/smaato/accept?id=1001&endpoint=https://google.com
2. Validation of the query parameters,
    - SUCCESS - "ok"
    - FAILED - "failed"
3. Every GET would mark the id in the redis and increment the unique-request property. 
   This is for id deduplication in case service is behind loadbalancer.  
4. A Scheduled thread would log the unique-request count to log file. - currentdir/app.log
5. If "endpoint" query parameters, POST would be issued and HTTP response code would be logged.

**For Example**

[INFO ] 2022-06-05 20:32:00.170 [vert.x-eventloop-thread-1] App - 10
[INFO ] 2022-06-05 20:33:00.169 [vert.x-eventloop-thread-1] App - 5
[INFO ] 2022-06-05 20:34:00.168 [vert.x-eventloop-thread-1] App - 17
[INFO ] 2022-06-05 20:35:00.172 [vert.x-eventloop-thread-1] App - 12
[INFO ] 2022-06-05 20:35:00.172 [vert.x-eventloop-thread-1] App - Endpoint response: 405 

For simplicity, User authentication and Authorization has been not
provided. As name suggests, it’s not a UI but backend.


**Prerequisites:**
-   Run Redis docker locally
    docker run --name my-redis -p 6379:6379 -d redis
    docker ps
    
-   Install JDK 11

-   Install maven 3 (settings.xml is provided in case you face issue connecting to publc repo)

**How to build:**

cd to path where application is downloaded/cloned.

-   cd smaato

-   mvn clean install

-   mvn spring-boot:run

