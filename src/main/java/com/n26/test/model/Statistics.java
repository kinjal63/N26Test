package com.n26.test.model;

/**
 * Created by kinjal.patel on 21/7/18
 */
public class Statistics {	
    private long count;
    private double sum;
    private double min;
    private double max;
    private double average;

    public Statistics(){
        this.count = 0;
        this.sum = 0;
        this.min = Integer.MAX_VALUE;
        this.max = Integer.MIN_VALUE;
        updateAverage();
    }

    public Statistics(Statistics obj) {
        this.count = obj.count;
        this.max = obj.max;
        this.min = obj.min;
        this.sum = obj.sum;
        updateAverage();
    }


    public Statistics(Statistics s1, Statistics s2) {
        this.max = Math.max(s1.max, s2.max);
        this.min = Math.min(s1.min, s2.min);
        this.count = s1.count+s2.count;
        this.sum = s1.sum+s2.sum;
        updateAverage();
    }

    public void reset() {
        this.count = 0;
        this.sum = 0;
        this.min = Integer.MAX_VALUE;
        this.max = Integer.MIN_VALUE;
        updateAverage();
    }

    public long getCount() {
        return count;
    }

    private void incrementCount() {
        count++;
    }

    public double getSum() {
        return sum;
    }

    private void updateSum(double transactionAmount) {
        sum = sum + transactionAmount;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    private void updateMin(double min) {
        if(min < this.min)
            this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    private void updateMax(double max) {
        if(max > this.max)
            this.max = max;
    }

    public double getAverage() {
        return average;
    }

    /**
     * Returns average transaction amount for this interval
     * @return
     */
    public void updateAverage() {
        if(count == 0)
            this.average = 0;
        else
            this.average= sum/count;
    }

    /**
     * Updates the following statistics for the second
     * 1. Sum amount
     * 2. Average amount
     * 3. Min amount
     * 4. Max amount
     * 5. Transaction count
     * @param transactionAmount
     */
    public void updateStatisticsWithTransaction(double transactionAmount) {
        this.updateSum(transactionAmount);
        this.updateMin(transactionAmount);
        this.updateMax(transactionAmount);
        this.incrementCount();
        this.updateAverage();
    }

    public void accumulateStatistics(Statistics statistics) {
        this.sum += statistics.sum;
        this.count += statistics.count;
        this.max = Math.max(max, statistics.max);
        this.min = Math.min(min, statistics.min);
        updateAverage();
    }


    @Override public String toString() {
        return "SecondStatistics{" + "count=" + count + ", sum=" + sum + ", min=" + min + ", max="
            + max + ", average=" + average + '}';
	    }
}
