package ru.otus.protobuf;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.ClientRequest;
import ru.otus.protobuf.generated.RemoteDBServiceGrpc;
import ru.otus.protobuf.generated.ServerResponse;

public class RemoteServiceImpl extends RemoteDBServiceGrpc.RemoteDBServiceImplBase{

    @Override
    public void getNextNumbers(ClientRequest request, StreamObserver<ServerResponse> responseObserver) {
        for (int i = request.getFirstNumber(); i <= request.getLastNumber(); i++){
            ServerResponse response = ServerResponse.newBuilder()
                    .setNextNumbers(i+1)
                    .build();
            try {
                Thread.sleep(2000);
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();

    }
}