package com.algotrader.interview.studies;

import com.algotrader.interview.data.Candle;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.reactivestreams.Publisher;

import java.util.Vector;

public class StdDev implements FlowableTransformer<Candle, Candle> {

    private String key;
    private int periods;

    private int currentPeriod = 0;

    private Vector<Candle> candleBuffer = new Vector<>();

    public StdDev (String key, int periods) {
        this.key = key;
        this.periods = periods;
    }

    public void reset () {
        this.currentPeriod = 0;
        this.candleBuffer.clear();
    }

    @Override
    public Publisher<Candle> apply(Flowable<Candle> flowable) {

        return flowable.map(candle ->  {

            if (currentPeriod < periods -1) {

                candle.setStudyValue(this.key, 0D);
                candleBuffer.add(candle);
                currentPeriod ++;

                return candle;
            }

            double movStdDev = 0D;
            double mean = 0D;

            candleBuffer.add(candle);

            for (int i = currentPeriod - periods + 1; i < currentPeriod + 1; i++) {
                mean = mean + candleBuffer.get(i).getClose();
            }

            mean = mean / periods;

            for (int i = currentPeriod - periods + 1; i < currentPeriod + 1; i++) {
                double toSqrt = candleBuffer.get(i).getClose() - mean;
                movStdDev = movStdDev + ((toSqrt) * (toSqrt));
            }


            movStdDev = movStdDev / periods;
            movStdDev = Math.sqrt(movStdDev);

            currentPeriod ++;

            candle.setStudyValue(this.key, movStdDev);

            return candle;
        });

    }
}
