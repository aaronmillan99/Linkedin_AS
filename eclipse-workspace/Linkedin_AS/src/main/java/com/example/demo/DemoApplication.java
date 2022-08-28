package com.example.demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	
	@Autowired
	JobDetails jobdetails;

	public static String linkedinURL = "https://www.linkedin.com/jobs/search/";
	public static ArrayList<String> consultedJobsId = new ArrayList<>();
	
	public ArrayList<ResultFilter> activeSearch(SearchFilters sf) {
		ArrayList<ResultFilter> rf = new ArrayList<ResultFilter>();
		try {
			
			rf = connectDefaultURL(linkedinURL, sf);
        	
		} catch (IOException e) {
        	e.printStackTrace();
			System.out.println("No matching jobs found.");		
		}
		return rf;
	}
	
	public static String setKeywords(String job, String location) {
		String[] str = job.split("\\s+");
		String keywords = "keywords=";
		for (int index = 0; index< str.length; index++) {	
			if(index==str.length-1) {
				keywords += str[index];
			}else {
				keywords += str[index]+"%20";
			}
		}
		keywords += "&location=" + location + "&sortBy=R";
		
		return keywords;
	}	
	
	public ArrayList<ResultFilter> connectDefaultURL(String urlConection, SearchFilters sf) throws IOException, FileNotFoundException {
		
		urlConection += setTimeKey(sf.getSearchTime());
		urlConection += setKeywords(sf.getJob(), sf.getLocation());
        System.out.println("Accessing: " +urlConection);
        
        
        URL url = new URL(urlConection);
        URLConnection conn = url.openConnection();
//        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        // open the stream and put it into BufferedReader
        InputStreamReader is = new InputStreamReader (conn.getInputStream());
        BufferedReader br = new BufferedReader(is);
       
        System.out.println("Searching...");

        ArrayList<ResultFilter> rf = new ArrayList<ResultFilter>();
        try {

          rf = linePrinterTEST(br, sf);
        	
        }catch(Exception e) {
        	e.printStackTrace();
        	System.out.println("Error en linePrinter");
        	 
        }finally {
        	
        	br.close();
        	is.close();
        }
        int[] nums = null;
        nums[0];
        System.out.println("Search Finished.");

        return rf;
	}
	
	public static String setTimeKey(int seconds) {
		return "?f_TPR=r" + seconds +"&";
	}	
	
	public ArrayList<ResultFilter> linePrinterTEST(BufferedReader br, SearchFilters sf) throws IOException, InterruptedException {
		
        String desirableLine;
        int pointer = 0;
    	ArrayList<ResultFilter> rfList = new ArrayList<ResultFilter>();

        while((desirableLine = br.readLine()) !=null) {
        	
        	ResultFilter rf = new ResultFilter();
        	int seconds = 0;
                if(desirableLine.contains(" ago") || desirableLine.contains("Just now")) {
                	
                	if(desirableLine.contains("minute ago") || desirableLine.contains("minutes ago")) {
                		seconds = Integer.parseInt(desirableLine.replaceAll("[^0-9]", ""))*60;
                	}else if(desirableLine.contains("hour ago") || desirableLine.contains("hours ago")) {
                		seconds = Integer.parseInt(desirableLine.replaceAll("[^0-9]", ""))*60*60;
                	}
                	
                	if(seconds<= sf.getSearchTime() && pointer != 0) {
                        rfList.get(pointer-1).setTime(String.valueOf(desirableLine.trim()));
                	}else {
                    	if(pointer != 0 && rfList.get(pointer-1).getTime() == null) {
                    		rfList.remove(pointer-1);
                    		pointer--;
                    	}
                	}
                
                
                }else if(desirableLine.contains("jobPosting:")) {
                	
                	String jobId = valueOfTag(desirableLine, "jobPosting:", "data-search-id=");
                	
                	rf.setJobId(jobId);
                	rfList.add(rf);
                	pointer++;
                	
                //
                }else if(desirableLine.contains("base-card__full-link")) {
                	
                	String urlJobDetails = valueOfTag(desirableLine, "href=", "data-tracking-control-name=");
                	rf.setDetailsURL(urlJobDetails);
                	rfList.get(pointer-1).setDetailsURL(urlJobDetails);
//                	if(urlJobDetails==null) {
//                		System.out.println("URL Detalle nulo");
//                	}
                	
                }
                
        }
        
        if(rfList.size()!=0) {
            for(int i = 0; i< rfList.size(); i++) {
            	
            	if(!consultedJobsId.contains(rfList.get(i).getJobId())) {
            		System.out.println(rfList.get(i).getDetailsURL());
            		if(rfList.get(i).getDetailsURL()==null) {
            			System.out.println("Valor nulo");
            			
            		}
            		
	            	JobDetailsDTO jobdetailsdto = jobdetails.connectURLDetails(rfList.get(i).getDetailsURL());
	            	rfList.get(i).setJobdetailsdto(jobdetailsdto);
	
	                System.out.println(rfList.get(i).getDetailsURL());
//	                System.out.println(rfList.get(i).getTime());
	                                
	                //add the jobs already opened to an array
	                consultedJobsId.add(rfList.get(i).getJobId());
	                            		
            	}else {
        			System.out.println("Job with Id "+ rfList.get(i).getJobId() + " already opened");
        			
            		rfList.remove(i);
            		i--;

            	}
            	
            }
        }else {
			System.out.println("Couldn't find results for that period of time");
        }
                
        return rfList;
	}
	
	public String valueOfTag(String line, String firstTag, String secondTag) {
		line = line.replaceAll("\\s+","");
		line = line.replaceAll("\"", "");

		String res = line.substring(line.indexOf(firstTag) + firstTag.length());
		res = res.substring(0, res.indexOf(secondTag));
		return res;
		
	}
	


	
}
