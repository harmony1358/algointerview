package com.algotrader.interview.studies;

import com.algotrader.interview.data.Studies;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.reactivestreams.Publisher;

import java.util.LinkedList;

public class MA implements FlowableTransformer<Studies, Studies> {

    private final String key;
    private final int periods;

    private final LinkedList<Double> window = new LinkedList<>();
    private Double sum = 0D;
    private int counter = 0;

    public MA(String key, int periods) {

        this.key = key;
        this.periods = periods;

    }

    public void reset () {
        this.sum = 0D;
        this.counter = 0;
        window.clear();
    }

    @Override
    public Publisher<Studies> apply(Flowable<Studies> flowable) {


        return flowable.map(studies -> {

            sum += studies.getCandle().getClose();
            window.add(studies.getCandle().getClose());

            sum = window.size() > periods ? sum - window.remove() : sum;
            counter = counter < periods ? counter + 1 : counter; // We don't increment counter if it reaches periods

            studies.setStudyValue(key, sum/counter);

            return studies;

        });

    }
}
