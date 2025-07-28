package org.acme.kafka.producer;

import avro.alumni.AlumniSchemaDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class AlumniProducer {


    @Inject
    @Channel("alumni-data")
    Emitter<AlumniSchemaDto> emitter;

    public void sendAlumni(AlumniSchemaDto alumni) {
        emitter.send(alumni);
    }
}
