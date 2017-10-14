package com.powersteeringsoftware.libs.util;

public class TimerWaiter {
    public static int DEFAULT_TIME_WAITER = 3000;

    long timeoutInMs;
    long stepInMs = 500;

    public TimerWaiter(int _intervalInMilliseconds) {
        timeoutInMs = _intervalInMilliseconds;
    }

    public TimerWaiter() {
        this(DEFAULT_TIME_WAITER);
    }

    public void waitTime() {
        long end = System.currentTimeMillis() + timeoutInMs;
        while (System.currentTimeMillis() < end) {
            try {
                Thread.sleep(stepInMs);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public long getIntervalInMilliseconds() {
        return timeoutInMs;
    }

    public void setIntervalInMilliseconds(long intervalInMilliseconds) {
        this.timeoutInMs = intervalInMilliseconds;
    }

    public long getTimeIntervalBetweenWaits() {
        return stepInMs;
    }

    public void setTimeIntervalBetweenWaits(long timeIntervalBetweenWaits) {
        this.stepInMs = timeIntervalBetweenWaits;
    }


    public static void waitTime(int mils) {
        new TimerWaiter(mils).waitTime();
    }

}
