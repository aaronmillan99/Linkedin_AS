package com.example.demo;

public class ResultFilter {

	public String jobId;
	public String time;
	public String jobURL;
	public String detailsURL;
	public JobDetailsDTO jobdetailsdto;
	
	public ResultFilter() {
		
	}
	
	public ResultFilter(String jobId, String time, String jobURL, String detailsURL, JobDetailsDTO jobdetailsdto) {
		
		this.jobId = jobId;
		this.time = time;
		this.jobURL = jobURL;
		this.detailsURL = detailsURL;
		this.jobdetailsdto = jobdetailsdto;
		
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getJobURL() {
		return jobURL;
	}

	public void setJobURL(String jobURL) {
		this.jobURL = jobURL;
	}

	public String getDetailsURL() {
		return detailsURL;
	}

	public void setDetailsURL(String detailsURL) {
		this.detailsURL = detailsURL;
	}

	public JobDetailsDTO getJobdetailsdto() {
		return jobdetailsdto;
	}

	public void setJobdetailsdto(JobDetailsDTO jobdetailsdto) {
		this.jobdetailsdto = jobdetailsdto;
	}
	
}
