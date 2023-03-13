package am.plotnikov.example.grpc;

import io.grpc.ManagedChannel;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.grpc.annotation.GrpcChannel;
import io.micronaut.grpc.server.GrpcServerChannel;

@Factory
public class Clients {

    @Bean
    MicronautExampleServiceGrpc.MicronautExampleServiceBlockingStub blockingStub(
            @GrpcChannel(GrpcServerChannel.NAME) ManagedChannel managedChannel
            ) {
        return MicronautExampleServiceGrpc.newBlockingStub(managedChannel);
    }

}
