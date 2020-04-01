package com.erfinfeluzy.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@GetMapping("/")
	public String greeting() {

		System.out.println("index controller");

		String result = "Hello World";
		
		for (int i = 0; i < 10; i++) {
			kafkaTemplate.send("test", "Data ke : " + i);
		}

		return result;

	}

}
