package com.ds.model;

import java.io.Serializable;


public class QuestionState implements Serializable{
    public QuestionState(long id, QStatEnum state) {
        this.qid = id;
        this.state = state;
    }
    public enum QStatEnum{
        UNINITIALIZED,
        INITIALIZED,
        CORRECT,
        INCORRECT, ATTEMPTED
    }
    private static final long serialVersionUID = -1763558961436949706L;
    private long qid;
    private QStatEnum state = QStatEnum.UNINITIALIZED;
    private long duration;
    public long getQid() {
        return qid;
    }
    public void setQid(long qid) {
        this.qid = qid;
    }
    public QStatEnum getState() {
        return state;
    }
    public void setState(QStatEnum state) {
        this.state = state;
    }
    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }
    
}
