package com.n26.test.dao;

import com.n26.test.model.Statistics;

/**
 * Created by kinjal.patel on 22/7/18
 */
public interface IStatisticsDao {
	Statistics readAggregatedStatistics();
    void clearStatistics(int index);
    void addTransactionStatistics(int index, double amount);
}
