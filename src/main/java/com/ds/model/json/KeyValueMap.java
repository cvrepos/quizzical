package com.ds.model.json;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ds.model.Question;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class KeyValueMap {
    private Map<String, Object> map = new HashMap<String, Object>();
    private static Gson gson = new Gson();
    //private static Type collectionType = new TypeToken<Map<String, String>>(){}.getType();
    public KeyValueMap(String key, String val){
        map.put(key, val);
    }
    public KeyValueMap() {
        // TODO Auto-generated constructor stub
    }
    public void put(String key, String val){
        map.put(key, val);
    }
    public String toJson(){        
        return gson.toJson(map);
    }
	public void put(String key, long n) {
		map.put(key, Long.toString(n));		
	}
	
	public void put(String key, List<Question> qs) {
		map.put(key, qs);		
	}
//	public static void main(String[] args){
//		KeyValueMap map = new KeyValueMap();
//		List<Event> t = new LinkedList<Event>();
//		t.add(new Event(1, "hi"));
//		t.add(new Event(2, "hello"));
//		map.put("events", t);
//		System.err.println(map.toJson());
//		
//		
//	}
    
}
