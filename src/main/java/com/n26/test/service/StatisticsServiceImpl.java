package com.n26.test.service;

import org.springframework.stereotype.Service;

import com.n26.test.dao.IStatisticsDao;
import com.n26.test.model.Statistics;
import com.n26.test.model.TransactionRequest;
import com.n26.test.utils.Utils;

/**
 * Created by kinjal.patel on 21/7/18
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService {
	private IStatisticsDao statisticsRepository;
	
	public StatisticsServiceImpl(IStatisticsDao statisticsRepository) {
		this.statisticsRepository = statisticsRepository;
	}

    @Override public void clearStatistics(int index) {
        statisticsRepository.clearStatistics(index);
    }

    @Override public void updateStatistics(TransactionRequest transactionData) {
        long timestampInSecs = Utils.roundOffToSeconds(transactionData.getTimeStamp());
        int index = Utils.getSecondSlot(timestampInSecs);
        statisticsRepository.addTransactionStatistics(index, transactionData.getAmount());
    }

    @Override public Statistics readCurrentStatistics() {
        return statisticsRepository.readAggregatedStatistics();
    }
}
