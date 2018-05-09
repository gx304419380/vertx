package com.fly.vertx.server;

import com.fly.vertx.dto.ResponseDto;
import com.fly.vertx.entity.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * @author XXX
 * @since 2018-05-09
 */
public class HelloWorldServer extends AbstractVerticle {
    // Convenience method so you can run it in your IDE
    public static void main(String[] args) {
        HelloWorldServer helloWorldServer = new HelloWorldServer();
        helloWorldServer.vertx = Vertx.vertx();
        helloWorldServer.start();
    }

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.get("/users/:name").handler(this::getUserByName);

        router.put("/users/:name").handler(this::insertUser);

        router.delete("/users/:name").handler(this::deleteUserByName);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080);

    }

    private void getUserByName(RoutingContext rc) {
        String name = rc.pathParams().get("name");
        System.out.println("get user: " + name);
        User user = new User(name, 35, "北京");
        rc.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(user));
    }

    private void deleteUserByName(RoutingContext rc) {
        String name = rc.pathParams().get("name");
        System.out.println("user deleted: " + name);
        rc.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end("{'status':'success'}");
    }

    private void insertUser(RoutingContext rc) {
        String name = rc.pathParams().get("name");
        System.out.println("user insert: " + name);
        ResponseDto<User> userResponseDto = new ResponseDto<>();
        userResponseDto.setData(new User(name, 20, "DC"));
        userResponseDto.setStatusCode(200);
        userResponseDto.setMessage("insert success");
        rc.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encode(userResponseDto));
    }

}
