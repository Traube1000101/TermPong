package org.traube;

public class Timer {

    public static final double ONE_SECOND = java.util.concurrent.TimeUnit.SECONDS.toNanos(1);

    private long start;
    private long[] ticks = new long[1000];
    private int first, last;
    private int fps;

    public Timer(){}

    public void start(){
        this.start = System.nanoTime();
    }

    public void tick(){
        ticks[last] = System.nanoTime();
        while (ticks[last] - ticks[first] > ONE_SECOND){
            first = circular(first + 1);
            fps--;
        }
        last = circular(last + 1);
        fps++;
    }

    public long tock(){
        return System.nanoTime() - ticks[circular(last-1)];
    }

    public long getElapsedNanoTime(){
        long prev = ticks[circular(last-2)];
        return (prev == 0) ? 0 : ticks[circular(last-1)] - prev;
    }

    public double getElapsedTime(){
        return this.getElapsedNanoTime() / ONE_SECOND;
    }

    public long getNanoTime(){
        return System.nanoTime() - this.start;
    }

    public double getTime(){
        return this.getNanoTime() / ONE_SECOND;
    }

    public int getFPS(){
        return this.fps;
    }

    private int circular(int n){
        return Math.floorMod(n, ticks.length);
    }
}