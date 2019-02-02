package com.algotrader.interview.execution;

import ch.algotrader.entity.Position;
import ch.algotrader.entity.trade.MarketOrder;
import ch.algotrader.enumeration.Direction;
import ch.algotrader.simulation.Simulator;
import com.algotrader.interview.strategy.Signal;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;

public class StrategyExecutor implements FlowableTransformer<Signal, Execution> {

    private static Logger LOG = LogManager.getLogger(StrategyExecutor.class);

    private final Simulator simulator;
    public StrategyExecutor(Simulator simulator, double initialCashBalance) {

        this.simulator = simulator;
        this.simulator.setCashBalance(initialCashBalance);
    }

    @Override
    public Publisher<Execution> apply(Flowable<Signal> flowable) {

        return flowable.map(signal -> {

            simulator.setCurrentPrice(signal.getPrice());

            return executeSignal(signal);

        });

    }

    private Execution executeSignal (Signal signal) {

        switch (signal.getSide()) {

            case BUY: return this.executeBuy(signal);
            case SELL: return this.executeSell(signal);
            case EXIT_LONG: return this.executeExitLong(signal);
            case EXIT_SHORT: return this.executeExitShort(signal);
            case DO_NOTHING: return this.executeDoNothing(signal);
            default: return this.executeDoNothing(signal);

        }

    }

    private Execution executeDoNothing (Signal signal) {
        return new Execution(signal.getStamp(), signal, -1d, ExecutionResult.SKIPPED);
    }

    private Execution executeBuy (Signal signal) {

        LOG.debug("Executing Signal: BUY, Quant: 1");

        simulator.sendOrder(new MarketOrder(ch.algotrader.enumeration.Side.BUY, 1));
        return new Execution(signal.getStamp(), signal, -1d, ExecutionResult.OK);
    }

    private Execution executeSell (Signal signal) {

        LOG.debug("Executing Signal: SELL, Quant: 1");

        simulator.sendOrder(new MarketOrder(ch.algotrader.enumeration.Side.SELL, 1));
        return new Execution(signal.getStamp(), signal, -1d, ExecutionResult.OK);
    }

    private Execution executeExitLong (Signal signal) {

        Position p = simulator.getPosition();
        if (p == null) {

            LOG.debug("Rejecting Signal: EXIT_LONG, reason: no positions to exit");

            return new Execution(signal.getStamp(), signal, -1d, ExecutionResult.REJECTED);

        }

        if (p.getDirection() == Direction.LONG) {

            long quant = p.getQuantity();
            if (quant > 0) {

                LOG.debug("Executing Signal: EXIT_LONG, Quant: " + quant);

                simulator.sendOrder(new MarketOrder(ch.algotrader.enumeration.Side.SELL, quant));
                double cacheBalance = simulator.getCashBalance();
                return new Execution(signal.getStamp(), signal, cacheBalance, ExecutionResult.OK);

            }
        }

        LOG.debug("Rejecting Signal: EXIT_LONG, reason: we are on SHORT direction");

        return new Execution(signal.getStamp(), signal, -1d, ExecutionResult.REJECTED);

    }

    private Execution executeExitShort (Signal signal) {

        Position p = simulator.getPosition();
        if (p == null) {

            LOG.debug("Rejecting Signal: EXIT_SHORT, reason: no positions to exit");

            return new Execution(signal.getStamp(), signal, -1d, ExecutionResult.REJECTED);
        }

        if (p.getDirection() == Direction.SHORT) {

            long quant = p.getQuantity();

            if (quant < 0) {

                LOG.debug("Executing Signal: EXIT_SHORT, Quant: " + (quant * -1));

                simulator.sendOrder(new MarketOrder(ch.algotrader.enumeration.Side.BUY, quant * -1));
                double cacheBalance = simulator.getCashBalance();
                return new Execution(signal.getStamp(), signal, cacheBalance, ExecutionResult.OK);
            }
        }

        LOG.debug("Rejecting Signal: EXIT_SHORT, reason: we are on LONG direction");

        return new Execution(signal.getStamp(), signal, -1d, ExecutionResult.REJECTED);
    }
}
