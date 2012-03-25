package com.ds.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ds.model.Module;
import com.ds.model.State;

public class ReviewAction implements Action{

	private Map<State, Action> nextActions = new HashMap<State,Action>();
	public ReviewAction(){
		nextActions.put(State.ACCEPTED, new PublishAction());
		nextActions.put(State.REJECTED, new UpdateAction());
	}
	public boolean CanPerform(Module module, Action action){
		if(nextActions.containsKey(module.getState())){
			Action nAction = nextActions.get(module.getState());
			if(action.equals(nAction)){
				return true;
			}
		}
		return false;
	}
	public Collection<Action> GetNextActions(){
		return nextActions.values();
	}
	
	
	
}
