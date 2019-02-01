package com.algotrader.interview.strategy;

import com.algotrader.interview.data.Studies;
import com.algotrader.interview.studies.BollingerBands;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.reactivestreams.Publisher;

public class SimpleBollingerStrategy implements FlowableTransformer<Studies, Signal> {

    private final String instrument;
    private final int periods;
    private final double deviations;
    private double previousPrice;
    private double previousUpper;
    private double previousLower;
    private double previousMiddle;

    public SimpleBollingerStrategy(String instrument, int periods, double deviations) {

        this.instrument = instrument;
        this.periods = periods;
        this.deviations = deviations;

    }

    public void reset () {
        this.previousPrice = 0d;
        this.previousLower = 0d;
        this.previousMiddle = 0d;
        this.previousUpper = 0d;
    }

    @Override
    public Publisher<Signal> apply(Flowable<Studies> flowable) {
        return flowable
                .compose(new BollingerBands("BB", periods, deviations))
                .map(studies -> {

                    double currentPrice = studies.getCandle().getClose();
                    double bbUpper  = studies.getStudyValue("BB_UPPER");
                    double bbLower  = studies.getStudyValue("BB_LOWER");
                    double bbMiddle = studies.getStudyValue("BB_MIDDLE");

                    if (crossDown(previousPrice, currentPrice, previousMiddle, bbMiddle)) {

                        previousPrice = currentPrice;
                        previousUpper = bbUpper;
                        previousLower = bbLower;
                        previousMiddle = bbMiddle;
                        return new Signal(instrument, studies.getCandle().getStamp(), Side.EXIT_LONG, currentPrice);
                    }

                    if (crossUp(previousPrice, currentPrice, previousMiddle, bbMiddle)) {

                        previousPrice = currentPrice;
                        previousUpper = bbUpper;
                        previousLower = bbLower;
                        previousMiddle = bbMiddle;
                        return new Signal(instrument, studies.getCandle().getStamp(), Side.EXIT_SHORT, currentPrice);
                    }

                    if (crossUp(previousPrice, currentPrice, previousLower, bbLower)) {

                        previousPrice = currentPrice;
                        previousUpper = bbUpper;
                        previousLower = bbLower;
                        previousMiddle = bbMiddle;
                        return new Signal(instrument, studies.getCandle().getStamp(), Side.BUY, currentPrice);
                    }

                    if (crossDown(previousPrice, currentPrice, previousUpper, bbUpper)) {

                        previousPrice = currentPrice;
                        previousUpper = bbUpper;
                        previousLower = bbLower;
                        previousMiddle = bbMiddle;
                        return new Signal(instrument, studies.getCandle().getStamp(), Side.SELL, currentPrice);
                    }


                    previousPrice = currentPrice;
                    previousUpper = bbUpper;
                    previousLower = bbLower;
                    previousMiddle = bbMiddle;
                    return new Signal(instrument, studies.getCandle().getStamp(), Side.DO_NOTHING, currentPrice);


                });
    }

    private boolean crossUp (double pv, double cv, double pbv, double cbv) {

        return Double.compare(pv, pbv) > 0 && Double.compare(cv, cbv) < 0;

    }

    private boolean crossDown (double pv, double cv, double pbv, double cbv) {

        return Double.compare(pv, pbv) < 0 && Double.compare(cv, cbv) > 0;

    }
}
