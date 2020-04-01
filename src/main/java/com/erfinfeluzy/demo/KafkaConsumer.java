package com.erfinfeluzy.demo;

import java.util.Date;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

	@KafkaListener(topics = "dbserver1.inventory.customers", groupId = "consumer-group-id-1")
	public void listen(
			@Payload String message,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition
			) 
	{
	   System.out.println("Received Message in group : [consumer-group-id-1]; "
	   		+ "Partition:"+ partition +"; "
	   		+ "[" + new Date() + "] - message: " + message);
	}
	
}
