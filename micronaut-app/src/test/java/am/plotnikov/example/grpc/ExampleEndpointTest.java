package am.plotnikov.example.grpc;

import am.plotnikov.example.jooq.enums.TmpStatus;
import am.plotnikov.example.jooq.tables.records.TmpRecord;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.jooq.DSLContext;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static am.plotnikov.example.jooq.Tables.TMP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest
public class ExampleEndpointTest {

    @Inject
    DSLContext dslContext;
    @Inject
    EmbeddedApplication<?> application;
    @Inject
    MicronautExampleServiceGrpc.MicronautExampleServiceBlockingStub micronautExampleStub;

    @Test
    void testCreateAndGet() {
        // GIVEN:

        // WHEN:
        MicronautCreateReply heyFromTest = micronautExampleStub.create(
                MicronautCreateRequest.newBuilder()
                        .setName("Yahoo")
                        .build()
        );

        MicronautGetReply micronautGetReply = micronautExampleStub.get(
                MicronautGetRequest.newBuilder()
                        .setUid(heyFromTest.getUid())
                        .build()
        );

        // THEN:
        assertEquals("Yahoo", micronautGetReply.getName());
        TmpRecord tmpRecord = dslContext.selectFrom(TMP)
                .where()
                .and(TMP.UID.eq(UUID.fromString(heyFromTest.getUid())))
                .fetchOne();
        assertEquals(heyFromTest.getUid(), tmpRecord.getUid().toString());
        assertNotNull(tmpRecord.getCreatedDate());
        assertNotNull(tmpRecord.getLastModifiedDate());
        assertEquals(1, tmpRecord.getVersion());
        assertEquals("Yahoo", tmpRecord.getName());
        assertEquals(TmpStatus.OK, tmpRecord.getStatus());
    }

}
