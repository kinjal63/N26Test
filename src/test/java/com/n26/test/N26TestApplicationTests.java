package com.n26.test;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.test.model.Statistics;
import com.n26.test.model.TransactionRequest;

/**
 * Created by kinjal.patel on 21/7/18
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class N26TestApplicationTests {
	@LocalServerPort
	private int port;
	
	@Autowired
	TestRestTemplate restTemplate;
	@Autowired
	TransactionController transactionController;
	
	@Before
	public void setUp() {
		Assertions.assertThat(restTemplate).isNotEqualTo(null);
		Assertions.assertThat(transactionController).isNotEqualTo(null);
	}
	
	@Test
	public void postTransaction() {
		TransactionRequest txRequest = new TransactionRequest(30.20, System.currentTimeMillis());
		ResponseEntity<Void> response = restTemplate.postForEntity(constructUrl("/transactions"), txRequest, Void.class);
		
		Assertions.assertThat(response).isNotEqualTo(null);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}
	
	@Test
	public void getStatistics() {	
		postTransaction();
		postTransaction();

		// Get statistics
		ResponseEntity<Statistics> response = restTemplate.getForEntity(constructUrl("/statistics"), Statistics.class);

		Assertions.assertThat(response).isNotEqualTo(null);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody().getCount()).isEqualTo(2);
	}
	
	private String constructUrl(String path) {
		return "http://localhost:" + port + path; 
	}

}
