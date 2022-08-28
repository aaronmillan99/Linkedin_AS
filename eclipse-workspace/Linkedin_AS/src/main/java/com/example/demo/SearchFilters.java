package com.example.demo;

import java.util.ArrayList;

public class SearchFilters {

	public String job;
	public String location;
	public int SearchTime;
	public ArrayList<ResultFilter> rf;
	
	public SearchFilters() {
		
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getSearchTime() {
		return SearchTime;
	}

	public void setSearchTime(int searchTime) {
		SearchTime = searchTime;
	}

	public ArrayList<ResultFilter> getRf() {
		return rf;
	}

	public void setRf(ArrayList<ResultFilter> rf) {
		this.rf = rf;
	}
	
}
