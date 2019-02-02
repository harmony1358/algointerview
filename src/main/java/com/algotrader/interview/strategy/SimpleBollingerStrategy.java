package com.algotrader.interview.strategy;

import com.algotrader.interview.studies.BollingerBands;
import com.algotrader.interview.studies.StudyEnvelope;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;

public class SimpleBollingerStrategy implements FlowableTransformer<StudyEnvelope, Signal> {

    private static final Logger LOG = LogManager.getLogger(SimpleBollingerStrategy.class);

    private final String instrument;
    private final String valueKey;
    private final int periods;
    private final double deviations;

    private double previousPrice;
    private double previousUpper;
    private double previousLower;
    private double previousMiddle;

    public SimpleBollingerStrategy(String instrument, String valueKey, int periods, double deviations) {

        this.instrument = instrument;
        this.valueKey = valueKey;
        this.periods = periods;
        this.deviations = deviations;

    }

    @Override
    public Publisher<Signal> apply(Flowable<StudyEnvelope> flowable) {
        return flowable
                .compose(new BollingerBands("BB", valueKey, periods, deviations))
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
                               double price, double bbUpper, double bbLower, double bbMiddle) {

        previousPrice = price;
        previousUpper = bbUpper;
        previousLower = bbLower;
        previousMiddle = bbMiddle;

        if (side != Side.DO_NOTHING)
            LOG.debug("Emitting Signal: " + side + " at " + price);

        return new Signal(instrument, stamp, side, price);

    }

    private boolean crossUp (double pv, double cv, double pbv, double cbv) {

        return Double.compare(pv, pbv) > 0 && Double.compare(cv, cbv) < 0;

    }

    private boolean crossDown (double pv, double cv, double pbv, double cbv) {

        return Double.compare(pv, pbv) < 0 && Double.compare(cv, cbv) > 0;

    }
}
