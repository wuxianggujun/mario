package org.wuxianggujun.jade.util;

public class Time {
    public static float timeStarted =  System.nanoTime();

    /**
     * Returns the elapsed time in seconds since the application started.
     *
     * @return the elapsed time in seconds
     */
    public static float getTime() {
        return (float) ((System.nanoTime() - timeStarted) * 1E-9);
    }
}
