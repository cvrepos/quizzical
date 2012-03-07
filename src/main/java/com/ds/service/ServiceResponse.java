package com.ds.service;

public class ServiceResponse {
    private boolean status;
    private String  metaData;
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public String getMetaData() {
        return metaData;
    }
    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }
    

}
