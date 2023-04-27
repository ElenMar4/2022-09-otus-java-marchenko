package ru.otus.protobuf;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GRPCServer {
    public static final int SERVER_PORT = 8080;
    public static void main(String[] args) throws IOException, InterruptedException {
        var server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new RemoteServiceImpl())
                .build();

        server.start();
        System.out.println("------Сервер готов к работе-------");
        server.awaitTermination();
    }
}
