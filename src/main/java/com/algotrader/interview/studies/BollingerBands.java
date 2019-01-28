package com.algotrader.interview.studies;

import com.algotrader.interview.data.Candle;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.reactivestreams.Publisher;

public class BollingerBands implements FlowableTransformer<Candle, Candle> {

    private String key;

    private int     periods;
    private double  deviations;

    public BollingerBands (String key, int periods, double deviations) {

        this.key = key;
        this.periods = periods;
        this.deviations = deviations;

    }

    @Override
    public Publisher<Candle> apply(Flowable<Candle> flowable) {
        return flowable
                .compose(new MA(this.key + "_MA", this.periods))
                .compose(new StdDev(this.key + "_SD", this.periods))
                .map(candle -> {

                    double ma = candle.getStudyValue(this.key + "_MA");
                    double sd = candle.getStudyValue(this.key + "_SD");
                    double dv = sd * this.deviations;

                    double upper    = ma + dv;
                    double lower    = ma - dv;
                    double middle   = ma;

                    candle.setStudyValue(this.key + "_UPPER", upper);
                    candle.setStudyValue(this.key + "_LOWER", lower);
                    candle.setStudyValue(this.key + "_MIDDLE", middle);

                    return candle;
                });
    }
}
