package com.example.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class Parameters {
	
	public SearchFilters setParams(String params) {
		
		String [] results = params.split("\\+");
		SearchFilters s = new SearchFilters();
		//Job name
		if(results[0]!="" && results[0]!=null) {
			s.setJob(results[0]);
		}
		// Job location
		if(results[1]!="" && results[1]!=null) {
			s.setLocation(results[1]);
		}
		//Time
		if(results[2]!="" && results[2]!=null) {
			s.setSearchTime(Integer.parseInt(results[2]));
		}
		return s;
	}

}
