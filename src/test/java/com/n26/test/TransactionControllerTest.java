package com.n26.test;

import java.util.Timer;
import java.util.TimerTask;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.test.cron.BucketClearingJob;
import com.n26.test.model.Statistics;
import com.n26.test.model.TransactionRequest;

/**
 * Created by kinjal.patel on 21/7/18
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TransactionController.class)
public class TransactionControllerTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionControllerTest.class);
	private static long SLEEP_TIME = 12000;

	@Autowired
	TransactionController txController;
	
	@Autowired
	BucketClearingJob jobService;

	@Test
	public void testTxControllerBean() {
		Assertions.assertThat(txController != null);
	}

	@Test
	public void testTransactionLessThan60Seconds() {
		TransactionRequest txRequest = new TransactionRequest(30.20, System.currentTimeMillis());
		ResponseEntity<Void> response = txController.doTransaction(txRequest);
		Assertions.assertThat(response).isNotEqualTo(null);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}

	@Test
	public void testTransactionMoreThan60Seconds() {
		TransactionRequest txRequest = new TransactionRequest(30.20, System.currentTimeMillis() - 61 * 1000);
		ResponseEntity<Void> response = txController.doTransaction(txRequest);
		Assertions.assertThat(response).isNotEqualTo(null);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

	@Test
	public void testTransactionWithNegativeOrZeroAmount() {
		TransactionRequest txRequest = new TransactionRequest(0, System.currentTimeMillis() - 65 * 1000);
		ResponseEntity<Void> response = txController.doTransaction(txRequest);
		Assertions.assertThat(response).isNotEqualTo(null);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	public void testStatistics() throws InterruptedException {
		Timer timer = new Timer();
		
		startTimer(timer);
		long timestamp = System.currentTimeMillis();
		LOGGER.info(
				"Positive integration test, will take roughly 50 seconds as it involves waiting for older transactions to expire");
		// Insert a 50 seconds old transaction
		TransactionRequest t = new TransactionRequest(10.0, timestamp - 50000);
		Assert.assertEquals(HttpStatus.CREATED, txController.doTransaction(t).getStatusCode());

		// Insert a 40 seconds old transaction
		t = new TransactionRequest(20.0, timestamp - 40000);
		Assert.assertEquals(HttpStatus.CREATED, txController.doTransaction(t).getStatusCode());

		// Insert a 30 seconds old transaction
		t = new TransactionRequest(30.0, timestamp - 30000);
		Assert.assertEquals(HttpStatus.CREATED, txController.doTransaction(t).getStatusCode());

		// Get statistics, should include all 4
		Statistics statistics = txController.getStatistics().getBody();
		LOGGER.info("Stats: {}", statistics);
		Assert.assertEquals(3, statistics.getCount());
		Assert.assertEquals(60, statistics.getSum(), 0.00);
		Assert.assertEquals(20, statistics.getAverage(), 0.00);
		Assert.assertEquals(30, statistics.getMax(), 0.00);
		Assert.assertEquals(10, statistics.getMin(), 0.00);

		// Sleep so that transaction 1 results have been removed by now
		Thread.sleep(SLEEP_TIME);

		// Get statistics, should include last 3
		statistics = txController.getStatistics().getBody();
		LOGGER.info("Stats: {}", statistics);
		Assert.assertEquals(2, statistics.getCount());
		Assert.assertEquals(50, statistics.getSum(), 0.00);
		Assert.assertEquals(25, statistics.getAverage(), 0.00);
		Assert.assertEquals(30, statistics.getMax(), 0.00);
		Assert.assertEquals(20, statistics.getMin(), 0.00);

		// Sleep so that transaction 2 results have been removed by now
		Thread.sleep(SLEEP_TIME);

		// Get statistics, should include last 2
		statistics = txController.getStatistics().getBody();
		LOGGER.info("Stats: {}", statistics);
		Assert.assertEquals(1, statistics.getCount());
		Assert.assertEquals(30, statistics.getSum(), 0.00);
		Assert.assertEquals(30, statistics.getAverage(), 0.00);
		Assert.assertEquals(30, statistics.getMax(), 0.00);
		Assert.assertEquals(30, statistics.getMin(), 0.00);
		
		stopTimer(timer);
	}
	
	private void startTimer(Timer t) {
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				jobService.execute();
			}
		}, 1000);
	}
	
	private void stopTimer(Timer t) {
		if( t != null ) {
			t.cancel();
		}
	}

}
