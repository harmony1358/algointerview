package com.algotrader.interview.studies;

import com.algotrader.interview.data.Candle;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.reactivestreams.Publisher;

import java.util.LinkedList;

public class MA implements FlowableTransformer<Candle, Candle> {

    private String key;
    private int periods;

    private LinkedList<Double> window = new LinkedList<>();
    private Double sum = 0D;
    private int counter = 0;

    public MA(String key, int periods) {

        this.key = key;
        this.periods = periods;

    }

    public void reset () {
        this.sum = 0D;
        this.counter = 0;
    }

    @Override
    public Publisher<Candle> apply(Flowable<Candle> flowable) {


        return flowable.map(candle -> {

            sum += candle.getClose();
            window.add(candle.getClose());

            sum = window.size() > periods ? sum - window.remove() : sum;
            counter = counter < periods ? counter + 1 : counter; // We don't increment counter if it reaches periods

            candle.setStudyValue(key, sum/counter);

            return candle;

        });

    }
}
