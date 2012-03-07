package com.ds.model.json;

import java.util.Date;
import java.util.List;

import com.ds.model.Option;


public class BasicSearch {
    private String question;
    private List<String> tags;
    private List<String> fields;
    private int limit = 10;
    private Date startTime;
    private Date endTime;
    private Long lastId;
    
    private Long id;
   
    
    private boolean forUpdate = false;
    
    public List<String> getFields() {
        return fields;
    }
    public void setFields(List<String> fields) {
        this.fields = fields;
    }
    public int getLimit() {
        return limit;
    }
    public void setLimit(int limit) {
        this.limit = limit;
    }
    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    public void setForUpdate(boolean forUpdate) {
        this.forUpdate = forUpdate;
    }
    public boolean isForUpdate() {
        return forUpdate;
    }
    public Long getLastId() {
        return lastId;
    }
    public void setLastId(Long lastId) {
        this.lastId = lastId;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    

}
