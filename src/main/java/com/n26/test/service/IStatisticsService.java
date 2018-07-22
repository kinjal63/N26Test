package com.n26.test.service;

import com.n26.test.model.Statistics;
import com.n26.test.model.TransactionRequest;

/**
 * Created by kinjal.patel on 21/7/18
 */
public interface IStatisticsService {
	void updateStatistics(TransactionRequest req);
	Statistics readCurrentStatistics();
	void clearStatistics(int index);
}
