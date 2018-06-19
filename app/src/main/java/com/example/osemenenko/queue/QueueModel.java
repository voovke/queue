package com.example.osemenenko.queue;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class QueueModel extends RealmObject {

    public static final String ID = "id";
    public static final String SERVICE_ID = "serviceId";
    public static final String NUMBER = "number";


    @PrimaryKey
    private long id;

    private long number;
    private String serviceId;

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}