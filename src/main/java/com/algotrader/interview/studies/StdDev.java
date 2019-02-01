package com.algotrader.interview.studies;

import com.algotrader.interview.strategy.StudyEnvelope;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.reactivestreams.Publisher;

import java.util.LinkedList;

public class StdDev implements FlowableTransformer<StudyEnvelope, StudyEnvelope> {

    private final String key;
    private final String valueKey;
    private final int periods;

    private final LinkedList<Double> window = new LinkedList<>();
    private Double variations = 0D;
    private int counter = 0;

    public StdDev (String key, String valueKey, int periods) {
        this.key = key;
        this.valueKey = valueKey;
        this.periods = periods;
    }

    public void reset () {
        variations = 0D;
        counter = 0;
        window.clear();
    }

    @Override
    public Publisher<StudyEnvelope> apply(Flowable<StudyEnvelope> flowable) {

        return flowable
                .compose(new MA(this.key + "_MA", valueKey, periods))
                .map(studies ->  {

                    counter = counter < periods ? counter + 1 : counter; // We don't increment counter when it reaches periods

                    Double mean = studies.getStudyValue(this.key+"_MA");
                    Double variation = Math.pow(studies.getStudyValue(valueKey) - mean, 2);
                    variations += variation;
                    window.add(variation);

                    variations = window.size() > periods ? variations - window.remove() : variations;

                    studies.setStudyValue(this.key, variations / counter);

                    return studies;

                });

    }
}
