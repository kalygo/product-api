package com.myretail.productapi.framework.domain.entities;

import java.util.LinkedList;
import java.util.List;

import static com.myretail.productapi.framework.domain.entities.ProcessingReport.EventCode.*;
import static com.myretail.productapi.framework.domain.entities.ProcessingReport.EventType.ERROR;
import static com.myretail.productapi.framework.domain.entities.ProcessingReport.EventType.WARN;

public class ProcessingReport {
    List<Event> events = new LinkedList<>();

    public void addEvent(Event event){
        events.add(event);
    }

    public List<Event> getEvents() {
        return events;
    }

    public enum EventType {
        ERROR, WARN;
    }

    public enum EventCode{

        CODE_1000,
        CODE_1001,
        CODE_1002,
        CODE_1003
        ;

    }

    public enum Event {
        ERR_PRODUCT_DATA_NOT_AVAILABLE(ERROR, CODE_1000),
        WARN_PRODUCT_PRICE_DATA_NOT_AVAILABLE(WARN, CODE_1001),
        VALIDATION_ERROR(ERROR, CODE_1002),
        ERROR_WHILE_PROCESSING_REQUEST(ERROR, CODE_1003)
        ;

        private EventType eventType;
        private EventCode eventCode;

        Event(EventType eventType, EventCode eventCode){
            this.eventType=eventType;
            this.eventCode=eventCode;
        }

        public EventType getEventType() {
            return eventType;
        }

        public EventCode getEventCode() {
            return eventCode;
        }
    }
}
