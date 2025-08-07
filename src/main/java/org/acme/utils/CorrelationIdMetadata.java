package org.acme.utils;

public class CorrelationIdMetadata {
    private final String correlationId;

    public CorrelationIdMetadata(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCorrelationId() {
        return correlationId;
    }
}
