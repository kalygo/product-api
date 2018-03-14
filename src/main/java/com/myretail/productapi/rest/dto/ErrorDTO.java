package com.myretail.productapi.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myretail.productapi.framework.domain.entities.ProcessingReport.Event;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {
    private String type;
    private String code;
    private String message;
    private String moreInfo;

    public ErrorDTO(Event event, String moreInfo){
        super();
        this.type=event.getEventType().name();
        this.code=event.getEventCode().name();
        this.message=event.name();
        this.moreInfo=moreInfo;
    }

    public ErrorDTO(Event event) {
        super();
        this.type=event.getEventType().name();
        this.code=event.getEventCode().name();
        this.message=event.name();
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }
}
