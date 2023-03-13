package am.plotnikov.example.grpc.mapper;

import am.plotnikov.example.grpc.MicronautCreateRequest;
import am.plotnikov.example.grpc.MicronautGetReply;
import am.plotnikov.example.jooq.tables.records.TmpRecord;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(
        componentModel = "jsr330",
        injectionStrategy = CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface TmpMapper {

    @Mapping(target = "mergeFrom", ignore = true)
    @Mapping(target = "clearField", ignore = true)
    @Mapping(target = "clearOneof", ignore = true)
    @Mapping(target = "nameBytes", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "mergeUnknownFields", ignore = true)
    @Mapping(target = "allFields", ignore = true)
    MicronautGetReply toGrpc(TmpRecord record);

    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "status", expression = "java(am.plotnikov.example.jooq.enums.TmpStatus.OK)")
    @Mapping(target = "value1", ignore = true)
    @Mapping(target = "value2", ignore = true)
    @Mapping(target = "value3", ignore = true)
    @Mapping(target = "value4", ignore = true)
    @Mapping(target = "value5", ignore = true)
    @Mapping(target = "value6", ignore = true)
    TmpRecord toRecord(MicronautCreateRequest request);

}
