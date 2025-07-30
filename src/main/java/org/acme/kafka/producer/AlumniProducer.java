package org.acme.kafka.producer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.avro.AlumniDto;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class AlumniProducer {


    @Inject
    @Channel("alumni-data")
    Emitter<AlumniDto> emitter;

    public void sendAlumni(AlumniDto alumni) {
        emitter.send(alumni);
    }
}
