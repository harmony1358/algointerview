package com.algotrader.interview;

import com.algotrader.interview.data.Candle;
import com.algotrader.interview.strategy.SimpleBollingerStrategy;
import com.algotrader.interview.studies.StudyEnvelope;
import org.junit.Test;

import static com.algotrader.interview.strategy.Side.*;
import static junit.framework.TestCase.assertEquals;

public class StrategiesTest extends BaseTest {

    @Test
    public void shouldProperlyEmitSignalsFromBBStrategy () {

        class Handler {
            public Candle candle;
        }

        final Handler handler = new Handler();

        ds.start("TEST_CASE_BS")
                .map(candle -> {

                    handler.candle = candle;
                    return candle;

                })
                .map(StudyEnvelope::new)
                .compose(new SimpleBollingerStrategy("TEST", "CLOSE",30, 1.5))
                .map(signal -> {

                    double value = 0D;
                    value = signal.getSide() == BUY ? 1D : value;
                    value = signal.getSide() == SELL ? -1D : value;
                    value = signal.getSide() == DO_NOTHING ? 0D : value;
                    value = signal.getSide() == EXIT_LONG ? 2D : value;
                    value = signal.getSide() == EXIT_SHORT ? -2D : value;

                    assertEquals(0, Double.compare(
                            handler.candle.getOpen(),
                            value
                    ));

                    return signal;

                })
                .test()
                .assertNoErrors();

    }

}
