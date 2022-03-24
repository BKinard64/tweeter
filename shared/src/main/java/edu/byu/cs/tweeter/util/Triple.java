package edu.byu.cs.tweeter.util;

public class Triple<F, S, T> extends Pair<F, S> {
    private T third;

    public Triple(F first, S second, T third) {
        super(first, second);
        this.third = third;
    }

    public T getThird() {
        return third;
    }

    public void setThird(T third) {
        this.third = third;
    }
}
