package org.cloudbus.cloudsim.mkg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CloudletStateHistory {
	
	HashMap<String, Double> hm; 
	public CloudletStateHistory() {
		// TODO Auto-generated constructor stub
		hm = new HashMap<String, Double>();
	}
	
	//List<HashMap<String,String>> cl = new ArrayList<HashMap<String,String>>();
	//HashMap<String, Double> x = new HashMap<String, Double>();
	//x.p
	public void addToMap(String time, double val) {
		hm.put(time, val);
	}
	
	public HashMap<String, Double> getMap() {
		return hm;
	}

	


}