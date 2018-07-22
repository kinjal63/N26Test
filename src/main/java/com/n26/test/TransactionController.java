package com.n26.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.n26.test.model.Statistics;
import com.n26.test.model.TransactionRequest;
import com.n26.test.service.IStatisticsService;

/**
 * Created by kinjal.patel on 21/7/18
 */
@RestController
public class TransactionController {
	private IStatisticsService statisticsService;
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);
	
	public TransactionController(IStatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	@PostMapping(value="/transactions", consumes={"application/json"})
	public ResponseEntity<Void> doTransaction(@RequestBody TransactionRequest req) {
		long diff = System.currentTimeMillis() - req.getTimeStamp();
		if(diff < 0 || req.getAmount() <= 0) {
			LOGGER.error("Invalid request");
			return ResponseEntity.badRequest().build();
		}
		else if(diff > 60 * 1000) {
			return ResponseEntity.noContent().build();
		}
		statisticsService.updateStatistics(req);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping(value = "/statistics")
    public ResponseEntity<Statistics> getStatistics() {
        long timestamp = System.currentTimeMillis();
        Statistics statistics = statisticsService.readCurrentStatistics();
        if (statistics.getCount() == 0) {
        	statistics.setMax(0);
        	statistics.setMin(0);
        }
        LOGGER.info("Statistics for timestamp {} : {}", timestamp, statistics);
        return ResponseEntity.status(HttpStatus.OK).body(statistics);
    }
}
