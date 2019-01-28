package com.algotrader.interview.studies;

import com.algotrader.interview.data.Candle;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.reactivestreams.Publisher;

public class MA implements FlowableTransformer<Candle, Candle> {

    private String key;
    private int periods;

    private int currentPeriod = 0;
    private Double previousClose = 0D;
    private Double previousValue = 0D;
    private Double cumulativeSum = 0D;

    public MA(String key, int periods) {

        this.key = key;
        this.periods = periods;

    }

    public void reset () {
        this.currentPeriod = 0;
        this.previousClose = 0D;
        this.previousValue = 0D;
        this.cumulativeSum = 0D;
    }

    @Override
    public Publisher<Candle> apply(Flowable<Candle> flowable) {


        return flowable.map(candle -> {

            if (currentPeriod < periods -1) {

                currentPeriod ++;
                previousValue = candle.getClose();
                previousClose = candle.getClose();
                cumulativeSum += candle.getClose();
                candle.setStudyValue(this.key, candle.getClose());

                return candle;

            }
            else if (currentPeriod == periods -1) {

                currentPeriod ++;
                cumulativeSum += candle.getClose();
                previousValue = cumulativeSum / periods;
                previousClose = candle.getClose();
                candle.setStudyValue(this.key, previousValue);

                return candle;

            }
            else {

                currentPeriod ++;
                double ma = previousClose + (candle.getClose() - previousValue) / periods;
                previousValue = ma;
                previousClose = candle.getClose();
                candle.setStudyValue(this.key, ma);

                return candle;
            }
        });

    }
}
