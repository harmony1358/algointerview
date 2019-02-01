package com.algotrader.interview;

import com.algotrader.interview.strategy.SimpleBollingerStrategy;
import org.junit.Test;

public class StrategiesTest extends BaseTest {

    @Test
    public void shouldProperlyEmitSignalsFromBBStrategy () {

        testStrategy("TEST_CASE_STRATEGY",
                new SimpleBollingerStrategy("EUR.USD", "CLOSE", 30, 1.5D));

    }

}
