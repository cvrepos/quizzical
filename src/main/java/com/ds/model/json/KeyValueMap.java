package com.ds.model.json;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class KeyValueMap {
    private Map<String, String> map = new HashMap<String, String>();
    private static Gson gson = new Gson();
    private static Type collectionType = new TypeToken<Map<String, String>>(){}.getType();
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
        return gson.toJson(map, collectionType);
    }
	public void put(String key, long n) {
		map.put(key, Long.toString(n));		
	}
    
}
