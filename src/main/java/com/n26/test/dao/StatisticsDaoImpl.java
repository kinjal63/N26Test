package com.n26.test.dao;

import org.springframework.stereotype.Repository;

import com.n26.test.model.Statistics;
import com.n26.test.utils.Constants;

/**
 * Created by kinjal.patel on 22/7/18
 */
@Repository
public class StatisticsDaoImpl implements IStatisticsDao {
	private Statistics secondBuckets[];
    private Statistics aggregatedStatistics;

    public StatisticsDaoImpl() {
        aggregatedStatistics = new Statistics();
        secondBuckets = new Statistics[Constants.INTERVAL];
        for(int i = 0; i < secondBuckets.length;i++)
            secondBuckets[i] = new Statistics();
    }

    /**
     *
     * Updates a second slot for the given transaction data and then updates the aggregated statistics
     *
     * @param index
     * @param amount
     */
    public synchronized void addTransactionStatistics(int index, double amount) {
    	Statistics targetSlotStatistics = secondBuckets[index];
        // Update these statistics by adding the new amount
        targetSlotStatistics.updateStatisticsWithTransaction(amount);
        aggregatedStatistics.accumulateStatistics(targetSlotStatistics);
    }

    /**
     * Clears the statistics for a second slot and then recalculates the aggregated statistics
     * @param index
     */
    public synchronized void clearStatistics(int index) {
        secondBuckets[index].reset();
        Statistics newAggregate = new Statistics();
        for(int i = 0; i < Constants.INTERVAL;i++) {
            if(secondBuckets[i].getCount() == 0)
                continue;
            Statistics currSlotStatistics = secondBuckets[i];
            newAggregate.accumulateStatistics(currSlotStatistics);
        }
        aggregatedStatistics = newAggregate;
    }

    /**
     * Reads the aggregated statistics
     * @return
     */
    public synchronized Statistics readAggregatedStatistics() {
        return new Statistics(aggregatedStatistics);
    }
}
