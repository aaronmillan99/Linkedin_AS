package com.example.demo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;

@RequestMapping("/api")
@SpringBootApplication
@RestController
public class Controller extends Parameters{
	
	@Autowired
	DemoApplication app;
	
	@RequestMapping(value = "/get/{params}")
	@ResponseBody
	public JSONObject index(@PathVariable("params") String params) {
		
		SearchFilters sf = setParams(params);
		
		JSONObject json = new JSONObject();
		ArrayList<ResultFilter> rf = app.activeSearch(sf);
//		rf.add(new ResultFilter("3180182987","1", "https://www.linkedin.com/jobs/search/?currentJobId=3180182987", null, null));
//		rf.add(new ResultFilter("3180182988","1", "https://www.linkedin.com/jobs/search/?currentJobId=3180182987", null, null));
//		rf.add(new ResultFilter("3180182989","1", "https://www.linkedin.com/jobs/search/?currentJobId=3180182987", null, null));
		json.put("key", rf);

		return json;
	}
		
}