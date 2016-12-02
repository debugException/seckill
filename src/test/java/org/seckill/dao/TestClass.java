package org.seckill.dao;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class TestClass {

	/*private HashMap<String, Integer> map = new HashMap<String, Integer>();
	
	public synchronized void add(String key){
		Integer value = map.get(key);
		if (value == null) {
			map.put(key, 1);
		}else {
			map.put(key, value+1);
		}
	}*/
	
	private ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();
	
	public void add(String key){
		Integer value = map.get(key);
		if (value == null) {
			map.put(key, 1);
		}else {
			map.put(key, value+1);
		}
	}
	
	public static void main(String[] args) {
		String state = "state=sign";
		String string = state.replaceAll("=", "=/?#");
		System.out.println(string);
	}
	
}
