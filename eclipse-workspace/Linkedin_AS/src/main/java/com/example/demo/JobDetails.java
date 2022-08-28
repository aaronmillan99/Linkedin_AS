package com.example.demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JobDetails {
	
//	@Autowired
//	DemoApplication da;
	
	public JobDetailsDTO connectURLDetails(String urlConection) throws IOException, FileNotFoundException {
		
        System.out.println("Accessing Details: " +urlConection);
        
        URL url = new URL(urlConection);
        URLConnection conn = url.openConnection();
//        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        // open the stream and put it into BufferedReader
        InputStreamReader is = new InputStreamReader (conn.getInputStream());
        BufferedReader br = new BufferedReader(is);
       
        System.out.println("-----------");
        System.out.println("Searching Details...");
        System.out.println("-----------");
        
    	JobDetailsDTO jobdetailsdto = new JobDetailsDTO();
        
        try {

        	jobdetailsdto = linePrinterJobDetails(br);
        	
        }catch(Exception e) {
        	
        	System.out.println("Error en linePrinterJobDetails");
        	 
        }finally {
        	br.close();
        	is.close();
        }
       
        System.out.println("----------------");
        System.out.println("Details Search Finished-");
        System.out.println("----------------");

        return jobdetailsdto;
	}
	
	public JobDetailsDTO linePrinterJobDetails(BufferedReader br) throws IOException {
		String description = "";
		
		String desirableLine;
		JobDetailsDTO jobdetailsdto = new JobDetailsDTO();
		int lineIndex = 1;
		while((desirableLine = br.readLine()) != null) {
			if (lineIndex == 67) {
//				System.out.println(desirableLine);
				desirableLine = deleteHtmlQuotationsFromString(desirableLine);
				description = valueOfTag(desirableLine, "description:", ",employmentType:");
				jobdetailsdto.setDescription(description);
//				System.out.println(desirableLine);
			}
			if(desirableLine.contains("<h3 class=\"sub-nav-cta__header\">")) {
				
				jobdetailsdto.setTittle(valueOfTag(desirableLine, "sub-nav-cta__header>", "</h3>"));
				break;
			}
			lineIndex++;
		}
		
		return jobdetailsdto;
		
	}
	
	public String valueOfTag(String line, String firstTag, String secondTag) {
		
		line = line.replaceAll("\"", "");

		String res = line.substring(line.indexOf(firstTag) + firstTag.length());
		res = res.substring(0, res.indexOf(secondTag));
		return res;
		
	}

	public String deleteHtmlQuotationsFromString(String line) {
		line = line.replaceAll("/", "");
		line = line.replaceAll("&lt;", "");
		line = line.replaceAll("p&gt;", "");
		line = line.replaceAll("&lt;", "");
		line = line.replaceAll("strong&gt;", "");
		line = line.replaceAll("br&gt;", "");
		line = line.replaceAll("ul&gt;", "");
		line = line.replaceAll("li&gt;", "");
		line = line.replaceAll("u&gt;", "");
		line = line.replaceAll("&gt;", "");
		line = line.replaceAll("&amp;", "");

		return line;
	}

	
}
