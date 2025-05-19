package main.timers;

/**
 * Wrapper class for the counter of the prescaler
 */
public class PrescalerCounter {
    private int counter = 0;

    public void increment() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }

    public void clear() {
        counter = 0;
    }
}
