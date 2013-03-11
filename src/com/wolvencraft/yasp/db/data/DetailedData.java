package com.wolvencraft.yasp.db.data;

import java.util.ArrayList;
import java.util.List;


public class DetailedData {
	
	public List<_DetailedData> data;
	
	public DetailedData() {
		data = new ArrayList<_DetailedData>();
	}
	
	public void add(_DetailedData newData) {
		data.add(newData);
	}
	
	public List<_DetailedData> get() {
		List<_DetailedData> temp = new ArrayList<_DetailedData>();
		for(_DetailedData value : data) temp.add(value);
		return temp;
	}
	
	public void clear() {
		data.clear();
	}
	
	public void remove(_DetailedData oldData) {
		data.remove(oldData);
	}
	
	public void sync(int playerId) {
		for(_DetailedData entry : get()) {
			if(entry.pushData(playerId)) remove(entry);
		}
	}
}