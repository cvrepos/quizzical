package com.ds.model;

import java.io.Serializable;

public class Option implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String ans;
    private boolean isCorrect = false;
    private int id;
    public Option(String answer, boolean isCorrect) {
        this.ans = answer;
        this.isCorrect = isCorrect;      
    }
    public Option() {
        // TODO Auto-generated constructor stub
    }
    public String getAns() {
        return ans;
    }
    public void setAns(String ans) {
        this.ans = ans;
    }
    public boolean isCorrect() {
        return isCorrect;
    }
    public void setCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    
}