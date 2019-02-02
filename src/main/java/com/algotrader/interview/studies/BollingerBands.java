package com.algotrader.interview.studies;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.reactivestreams.Publisher;

public class BollingerBands implements FlowableTransformer<StudyEnvelope, StudyEnvelope> {

    private final String key;
    private final String valueKey;

    private final int     periods;
    private final double  deviations;

    public BollingerBands (String key, String valueKey, int periods, double deviations) {

        this.key = key;
        this.valueKey = valueKey;
        this.periods = periods;
        this.deviations = deviations;

    }

    @Override
    public Publisher<StudyEnvelope> apply(Flowable<StudyEnvelope> flowable) {
        return flowable
                .compose(new StdDev(this.key + "_SD", valueKey, periods)) // We don't have to chain MA since it is calculated by StdDev
                .map(studies -> {

                    double ma = studies.getStudyValue(this.key + "_SD_MA"); // Fetch MA value from StdDev
                    double sd = studies.getStudyValue(this.key + "_SD");
                    double dv = sd * this.deviations;

                    double upper    = ma + dv;
                    double lower    = ma - dv;

                    studies.setStudyValue(this.key + "_UPPER", upper);
                    studies.setStudyValue(this.key + "_LOWER", lower);
                    studies.setStudyValue(this.key + "_MIDDLE", ma);

                    return studies;
                });
    }
}
