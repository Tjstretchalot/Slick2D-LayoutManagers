package me.timothy.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicMap<T1, T2> {
	private List<T1> allType1;
	private List<T1> unmodifiableType1;
	private List<T2> allType2;
	
	public BasicMap() {
		allType1 = new ArrayList<>();
		allType2 = new ArrayList<>();
	}
	
	public List<T1> getKeys() {
		if(unmodifiableType1 != null)
			return unmodifiableType1;
		return unmodifiableType1 = Collections.unmodifiableList(allType1);
	}
	
	public T2 get(T1 key) {
		return allType2.get(allType1.indexOf(key));
	}
	
	public void put(T1 key, T2 value) {
		allType1.add(key);
		allType2.add(value);
	}
}
