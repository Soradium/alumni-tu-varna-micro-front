package org.acme.kafka.producer;


import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.avro.AlumniDto;
import org.acme.avro.AlumniGroupDto;
import org.acme.utils.CorrelationIdMetadata;
import org.apache.kafka.streams.processor.api.Record;
import org.eclipse.microprofile.reactive.messaging.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;


@ApplicationScoped
public class AlumniProducer {

    @Inject
    @Channel("alumni-group-data")
    Emitter<Message<AlumniGroupDto>> emitter;

    private final ConcurrentHashMap<String, CompletableFuture<AlumniGroupDto>> pendingReplies
            = new ConcurrentHashMap<>(); //hash map for pending requests

    public CompletionStage<AlumniGroupDto> sendAndWaitForReply(AlumniGroupDto alumni) {

        String correlationId = UUID.randomUUID().toString(); //generation of UUID
        //we can't use alumniGroupDto id because with one dto we
        //can make different logic in parallel (updating, creating, deleting and so on)
        //so we use this generated UUID for concurrent handling of requests

        OutgoingKafkaRecordMetadata<String> metadata = OutgoingKafkaRecordMetadata.<String>builder()
                .withKey(correlationId)
                .build(); //create metadata for our request

        Message<AlumniGroupDto> message = Message.of(alumni)
                .addMetadata(metadata)
                .addMetadata(new CorrelationIdMetadata(correlationId)); //add this metadata with
        //request

        CompletableFuture<AlumniGroupDto> future = new CompletableFuture<>(); //create object, which will wait
        //until we close it
        pendingReplies.put(correlationId, future); //add message as a pending

        emitter.send(message); //send message

        return future; //return future

        //From this moment, this method become "frozen" until object future
        //is "open" for modification
    }

    //As soon as we receive reply from backend service kafka launch this code
    @Incoming("alumni-group-data-in")
    public void onReply(Message<AlumniGroupDto> message) {
        String correlationId = message.getMetadata(CorrelationIdMetadata.class)
                .map(CorrelationIdMetadata::getCorrelationId)
                .orElse(null); //extract UUID

        if(correlationId != null) { //check if it's not null
            CompletableFuture<AlumniGroupDto> future = pendingReplies.remove(correlationId);
            //if everything is correct remove object from pending with such UUID
            if (future != null) {
                //if future is not null we complete it and method sendAndWaitForReply return this object
                future.complete(message.getPayload());
            }
        }
    }
}
