package com.ds.model;

import java.util.LinkedList;
import java.util.List;

public enum State{
	DRAFT("Delete","AddQuestion", "SendForReview"),
	PUBLISHED("abc", "def"),
	INREVIEW(""),
	REJECTED(""),
	ACCEPTED(""),
	SUGGESTED("ahgshgjash");
	private final List<String> actions = new LinkedList<String>();
	State(String ... actions){
		for(String action:actions){
			this.actions.add(action);
		}
	}
	List<String> getActions(){
		return this.actions;
	}
}