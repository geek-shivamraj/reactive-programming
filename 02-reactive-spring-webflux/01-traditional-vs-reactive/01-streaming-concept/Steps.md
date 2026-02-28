# Traditional Vs Reactive

We will be using below jar file to demonstrate other service dependencies in our Microservices architecture.

External Service JAR: [this jar](https://github.com/vinsguru/spring-webflux-course/raw/master/01-external-services/external-services.jar)

## How To Run

```bash
java -jar external-services.jar
```
- It uses port `7070` by default. (http://localhost:7070/webjars/swagger-ui/index.html)

## To change the port

```bash
java -jar external-services.jar --server.port=8080
```

## Testing via Terminal

#### Traditional Controller: `curl http://localhost:8080/traditional/products`
#### Reactive Controller: `curl http://localhost:8080/reactive/products`

Scenario 1: Traditional service will take its time while Reactive service will start giving Product as it receives without buffering.

Scenario 2: In case user cancels the request in btw, Traditional Service will still complete the call while Reactive Service will be canceled.


## Testing via Browser

Scenario 1: Same as above

Scenario 2: In case user refresh the page, for traditional service with each refresh, a new API call will be invoked but 
for Reactive service with each refresh, previous API call will get cancel & a new API call will be invoked.