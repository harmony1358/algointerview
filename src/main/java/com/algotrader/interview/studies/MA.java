package com.algotrader.interview.studies;

import com.algotrader.interview.strategy.StudyEnvelope;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.reactivestreams.Publisher;

import java.util.LinkedList;

public class MA implements FlowableTransformer<StudyEnvelope, StudyEnvelope> {

    private final String key;
    private final String valueKey;
    private final int periods;

    private final LinkedList<Double> window = new LinkedList<>();
    private Double sum = 0D;
    private int counter = 0;

    public MA(String key, String valueKey, int periods) {

        this.key = key;
        this.valueKey = valueKey;
        this.periods = periods;

    }

    @Override
    public Publisher<StudyEnvelope> apply(Flowable<StudyEnvelope> flowable) {


        return flowable.map(studies -> {

            sum += studies.getStudyValue(valueKey);
            window.add(studies.getStudyValue(valueKey));

            sum = window.size() > periods ? sum - window.remove() : sum;
            counter = counter < periods ? counter + 1 : counter; // We don't increment counter if it reaches periods

            studies.setStudyValue(key, sum/counter);

            return studies;

        });

    }
}
