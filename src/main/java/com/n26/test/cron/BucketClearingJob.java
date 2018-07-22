package com.n26.test.cron;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.n26.test.TransactionController;
import com.n26.test.service.IStatisticsService;
import com.n26.test.utils.Constants;
import com.n26.test.utils.Utils;

/**
 * Created by kinjal.patel on 21/7/18
 */
@Service
public class BucketClearingJob {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);
	
    @Autowired private IStatisticsService statisticsService;
    private SimpleDateFormat CURRENT_TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");

    /**
     * Runs at the start of every second to reset the segment tree bucket for that second
     */
    @Scheduled(cron = "* * * * * *") public void execute() {
        MDC.put(Constants.JOB_NAME_LOG_KEY, Constants.BUCKET_CLEARING_JOB_NAME);
        long timestamp = System.currentTimeMillis();
        int index = Utils.getSecondSlot(Utils.roundOffToSeconds(timestamp));
        LOGGER.info("Running job timestamp:{}({}) on second bucket index {} ", timestamp,
            CURRENT_TIME_FORMATTER.format(new Date(timestamp)), index);
        statisticsService.clearStatistics(index);
        MDC.remove(Constants.JOB_NAME_LOG_KEY);
    }
}
