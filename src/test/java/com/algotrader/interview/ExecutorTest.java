package com.algotrader.interview;

import com.algotrader.interview.data.CSVDataSource;
import com.algotrader.interview.data.DataSource;
import com.algotrader.interview.execution.ExecutionResult;
import com.algotrader.interview.execution.StrategyExecutor;
import com.algotrader.interview.strategy.Side;
import com.algotrader.interview.strategy.Signal;
import com.algotrader.interview.strategy.SimpleBollingerStrategy;
import com.algotrader.interview.strategy.StudyEnvelope;
import org.junit.Test;

import static junit.framework.TestCase.assertSame;

public class ExecutorTest extends BaseTest {

    @Test
    public void shouldProperlyExecuteSignals () {

        class Handler {
            public Signal signal;
        }

        final Handler handler = new Handler();

        DataSource ds = new CSVDataSource();
        ds.start("TEST_CASE_DS")
                .map(StudyEnvelope::new)
                .compose(new SimpleBollingerStrategy("EUR.USD", "CLOSE",30, 1.5))
                .map(signal -> {

                    handler.signal = signal;
                    return signal;

                })
                .compose(new StrategyExecutor(1000000))
                .subscribe(execution -> {


                    if (handler.signal.getSide() == Side.BUY || handler.signal.getSide() == Side.SELL)
                        assertSame(execution.getResult(), ExecutionResult.OK);


                });

    }

}
