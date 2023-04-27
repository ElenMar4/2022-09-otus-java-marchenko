package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.ClientRequest;
import ru.otus.protobuf.generated.RemoteDBServiceGrpc;
import ru.otus.protobuf.generated.ServerResponse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final AtomicInteger responseNumber = new AtomicInteger(0);
    private static final int REQUEST_FIRST_NUMBER = 0;
    private static final int REQUEST_LAST_NUMBER = 20;
    private static final int NUMBER_OF_ITERATIONS = 50;


    public static void main(String[] args) {

        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var stub = RemoteDBServiceGrpc.newStub(channel);
        var latch = new CountDownLatch(1);

        var request = ClientRequest.newBuilder()
                .setFirstNumber(REQUEST_FIRST_NUMBER)
                .setLastNumber(REQUEST_LAST_NUMBER)
                .build();


        var responseObserver = new StreamObserver<ServerResponse>() {

            public int getNumberFromServer() {
                return responseNumber.getAndSet(0);
            }

            @Override
            public void onNext(ServerResponse value) {
                responseNumber.set(value.getNextNumbers());
                System.out.printf("          Сервер---> valueFromServer: %d %n", responseNumber.get());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t);
            }

            @Override
            public void onCompleted() {
                System.out.println("\n-------Сервер вернул все числа-------\n");
                latch.countDown();
            }
        };
        stub.getNextNumbers(request, responseObserver);

        int currentValue = 0;

        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++) {
            currentValue = currentValue + responseObserver.getNumberFromServer() + 1;
            System.out.printf("%d) Клиент---> currentValue: %d%n", i, currentValue);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        channel.shutdownNow();
    }
}
