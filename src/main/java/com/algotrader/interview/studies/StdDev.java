package com.algotrader.interview.studies;

import com.algotrader.interview.data.Candle;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.reactivestreams.Publisher;

import java.util.LinkedList;
import java.util.Vector;

public class StdDev implements FlowableTransformer<Candle, Candle> {

    private String key;
    private int periods;

    private LinkedList<Double> window = new LinkedList<>();
    private Double variations = 0D;
    private int counter = 0;

    public StdDev (String key, int periods) {
        this.key = key;
        this.periods = periods;
    }

    public void reset () {
        variations = 0D;
        counter = 0;
        window.clear();
    }

    @Override
    public Publisher<Candle> apply(Flowable<Candle> flowable) {

        return flowable
                .compose(new MA(this.key + "_MA", this.periods))
                .map(candle ->  {

                    counter = counter < periods ? counter + 1 : counter; // We don't increment counter when it reaches periods

                    Double mean = candle.getStudyValue(this.key+"_MA");
                    Double variation = Math.pow(candle.getClose() - mean, 2);
                    variations += variation;
                    window.add(variation);

                    variations = window.size() > periods ? variations - window.remove() : variations;

                    candle.setStudyValue(this.key, variations / counter);

                    return candle;

                });

    }
}
