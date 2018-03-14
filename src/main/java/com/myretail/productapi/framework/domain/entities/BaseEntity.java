package com.myretail.productapi.framework.domain.entities;

import static com.myretail.productapi.framework.domain.entities.BaseEntity.Status.ACTIVE;

public abstract class BaseEntity {

    private Status status = ACTIVE;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isActive(){
        return status==null || status.equals(ACTIVE);
    }

    public enum Status{
        ACTIVE, DELETED
    }


}
