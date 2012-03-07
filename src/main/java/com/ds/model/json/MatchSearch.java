package com.ds.model.json;

import java.util.LinkedList;
import java.util.List;

import com.ds.model.Option;

public class MatchSearch {
    private List<Option> answers;
    private Long id;
    public List<Option> getAnswers() {
        return answers;
    }
    public void setAnswers(List<Option> answers) {
        this.answers = answers;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setAnswers(String[] ids) {
        if(answers == null){
            answers = new LinkedList<Option>();
        }
        for(String id:ids){
            Option o = new Option();
            o.setId(Integer.parseInt(id));
            answers.add(o);            
        }
        
    }

}
