package com.algotrader.interview;

import ch.algotrader.entity.Position;
import ch.algotrader.entity.trade.MarketOrder;
import ch.algotrader.simulation.Simulator;
import ch.algotrader.simulation.SimulatorImpl;
import com.algotrader.interview.data.CSVDataSource;
import com.algotrader.interview.data.DataSource;
import com.algotrader.interview.strategy.StudyEnvelope;
import com.algotrader.interview.execution.ExecutionResult;
import com.algotrader.interview.execution.StrategyExecutor;
import com.algotrader.interview.strategy.Side;
import com.algotrader.interview.strategy.SimpleBollingerStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App 
{

    private static Logger LOG = LogManager.getLogger(App.class);
    public static void main(String[] argv) {

        final Simulator simulator = new SimulatorImpl();

        DataSource ds = new CSVDataSource();
        ds.start("EUR.USD")
                .map(StudyEnvelope::new)
                .compose(new SimpleBollingerStrategy("EUR.USD", "CLOSE",30, 1.5))
                .compose(new StrategyExecutor(simulator, 1000000))
                .subscribe(execution -> {

                    if ((execution.getResult() == ExecutionResult.OK)
                            && (execution.getSignal().getSide() == Side.EXIT_LONG
                            || execution.getSignal().getSide() == Side.EXIT_SHORT))

                        LOG.debug("Current balance: " + execution.getBalance());

                }, throwable -> {

                    LOG.debug("Strategy stopped due to: " + throwable.getMessage());

                    /*

                        Usually streams are unbreakable, however if something goes wrong
                        we would like to stop strategy and close all positions

                     */

                    Position p = simulator.getPosition();
                    if (p == null) return;

                    long quant = p.getQuantity();

                    if (quant > 0) {

                        LOG.debug("Emergency exit from LONG position, Quant: " + quant);
                        simulator.sendOrder(new MarketOrder(ch.algotrader.enumeration.Side.SELL, quant));

                    } else if (quant < 0) {

                        LOG.debug("Emergency exit from SHORT position, Quant: " + (quant * -1));
                        simulator.sendOrder(new MarketOrder(ch.algotrader.enumeration.Side.BUY, quant * -1));

                    }
                });

    }
}
