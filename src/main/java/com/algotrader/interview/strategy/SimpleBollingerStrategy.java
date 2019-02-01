package com.algotrader.interview.strategy;

import com.algotrader.interview.studies.BollingerBands;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.reactivestreams.Publisher;

public class SimpleBollingerStrategy implements FlowableTransformer<StudyEnvelope, Signal> {

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
    public Publisher<Signal> apply(Flowable<StudyEnvelope> flowable) {
        return flowable
                .compose(new BollingerBands("BB", "CLOSE", periods, deviations))
                .map(studies -> {

                    double currentPrice = studies.getStudyValue("CLOSE");
                    double bbUpper  = studies.getStudyValue("BB_UPPER");
                    double bbLower  = studies.getStudyValue("BB_LOWER");
                    double bbMiddle = studies.getStudyValue("BB_MIDDLE");

                    if (crossDown(previousPrice, currentPrice, previousMiddle, bbMiddle)) {

                        return emitSignal(instrument, studies.getStamp(), Side.EXIT_LONG,
                                            currentPrice, bbUpper, bbLower, bbMiddle);
                    }

                    if (crossUp(previousPrice, currentPrice, previousMiddle, bbMiddle)) {

                        return emitSignal(instrument, studies.getStamp(), Side.EXIT_SHORT,
                                            currentPrice, bbUpper, bbLower, bbMiddle);
                    }

                    if (crossUp(previousPrice, currentPrice, previousLower, bbLower)) {

                        return emitSignal(instrument, studies.getStamp(), Side.BUY,
                                            currentPrice, bbUpper, bbLower, bbMiddle);
                    }

                    if (crossDown(previousPrice, currentPrice, previousUpper, bbUpper)) {

                        return emitSignal(instrument, studies.getStamp(), Side.SELL,
                                            currentPrice, bbUpper, bbLower, bbMiddle);
                    }

                    return emitSignal(instrument, studies.getStamp(), Side.DO_NOTHING,
                                        currentPrice, bbUpper, bbLower, bbMiddle);

                });
    }

    private Signal emitSignal (String instrument, Long stamp, Side side,
                               Double price, Double bbUpper, Double bbLower, Double bbMiddle) {

        previousPrice = price;
        previousUpper = bbUpper;
        previousLower = bbLower;
        previousMiddle = bbMiddle;

        return new Signal(instrument, stamp, side, price);

    }

    private boolean crossUp (double pv, double cv, double pbv, double cbv) {

        return Double.compare(pv, pbv) > 0 && Double.compare(cv, cbv) < 0;

    }

    private boolean crossDown (double pv, double cv, double pbv, double cbv) {

        return Double.compare(pv, pbv) < 0 && Double.compare(cv, cbv) > 0;

    }
}
