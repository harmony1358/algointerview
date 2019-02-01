package com.algotrader.interview.studies;

import com.algotrader.interview.data.Studies;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.reactivestreams.Publisher;

import java.util.LinkedList;
import java.util.Vector;

public class StdDev implements FlowableTransformer<Studies, Studies> {

    private final String key;
    private final int periods;

    private final LinkedList<Double> window = new LinkedList<>();
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
    public Publisher<Studies> apply(Flowable<Studies> flowable) {

        return flowable
                .compose(new MA(this.key + "_MA", this.periods))
                .map(studies ->  {

                    counter = counter < periods ? counter + 1 : counter; // We don't increment counter when it reaches periods

                    Double mean = studies.getStudyValue(this.key+"_MA");
                    Double variation = Math.pow(studies.getCandle().getClose() - mean, 2);
                    variations += variation;
                    window.add(variation);

                    variations = window.size() > periods ? variations - window.remove() : variations;

                    studies.setStudyValue(this.key, variations / counter);

                    return studies;

                });

    }
}
