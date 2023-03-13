package am.plotnikov.example.grpc.endpoint;

import am.plotnikov.example.grpc.MicronautCreateReply;
import am.plotnikov.example.grpc.MicronautCreateRequest;
import am.plotnikov.example.grpc.MicronautExampleServiceGrpc;
import am.plotnikov.example.grpc.MicronautGetReply;
import am.plotnikov.example.grpc.MicronautGetRequest;
import am.plotnikov.example.grpc.mapper.TmpMapper;
import am.plotnikov.example.jooq.tables.records.TmpRecord;
import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jooq.DSLContext;

import java.util.UUID;

import static am.plotnikov.example.jooq.tables.Tmp.TMP;


@Singleton
public class ExampleEndpoint extends MicronautExampleServiceGrpc.MicronautExampleServiceImplBase {

    @Inject
    private DSLContext dslContext;
    @Inject
    private TmpMapper tmpMapper;

    @Override
    public void create(MicronautCreateRequest request, StreamObserver<MicronautCreateReply> responseObserver) {
        TmpRecord tmpRecord = dslContext.newRecord(TMP, tmpMapper.toRecord(request));
        tmpRecord.store();

        responseObserver.onNext(
                MicronautCreateReply.newBuilder()
                        .setUid(tmpRecord.getUid().toString())
                        .setMessage(String.format("Created, id=%s", tmpRecord.getUid()))
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void get(MicronautGetRequest request, StreamObserver<MicronautGetReply> responseObserver) {
        TmpRecord record = dslContext.selectFrom(TMP)
                .where()
                .and(TMP.UID.eq(UUID.fromString(request.getUid())))
                .fetchOne();

        responseObserver.onNext(
                tmpMapper.toGrpc(record)
        );
        responseObserver.onCompleted();
    }
}
