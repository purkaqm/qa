package com.powersteeringsoftware.core.util.waiters;

public class TimerWaiter {
	public static long DEFAULT_TIME_WAITER = 3000;

	long intervalInMilliseconds;
	long timeIntervalBetweenWaits = 500;

	public TimerWaiter(int _intervalInMilliseconds){
		intervalInMilliseconds = _intervalInMilliseconds;
	}

	public TimerWaiter(){
		intervalInMilliseconds = DEFAULT_TIME_WAITER;
	}

	public void waitTime(){
		long end = System.currentTimeMillis()+ intervalInMilliseconds;

		while (System.currentTimeMillis() < end) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
	}

	public long getIntervalInMilliseconds() {
		return intervalInMilliseconds;
	}

	public void setIntervalInMilliseconds(long intervalInMilliseconds) {
		this.intervalInMilliseconds = intervalInMilliseconds;
	}

	public long getTimeIntervalBetweenWaits() {
		return timeIntervalBetweenWaits;
	}

	public void setTimeIntervalBetweenWaits(long timeIntervalBetweenWaits) {
		this.timeIntervalBetweenWaits = timeIntervalBetweenWaits;
	}



}
