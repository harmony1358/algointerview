package com.algotrader.interview.studies;

import com.algotrader.interview.data.Studies;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.reactivestreams.Publisher;

public class BollingerBands implements FlowableTransformer<Studies, Studies> {

    private final String key;

    private final int     periods;
    private final double  deviations;

    public BollingerBands (String key, int periods, double deviations) {

        this.key = key;
        this.periods = periods;
        this.deviations = deviations;

    }

    @Override
    public Publisher<Studies> apply(Flowable<Studies> flowable) {
        return flowable
                .compose(new StdDev(this.key + "_SD", this.periods)) // We don't have to chain MA since it is calculated by StdDev
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
