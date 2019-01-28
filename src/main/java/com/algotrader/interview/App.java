package com.algotrader.interview;

import com.algotrader.interview.data.CSVDataSource;
import com.algotrader.interview.data.DataSource;
import com.algotrader.interview.execution.ExecutionResult;
import com.algotrader.interview.execution.StrategyExecutor;
import com.algotrader.interview.strategy.Side;
import com.algotrader.interview.strategy.SimpleBollingerStrategy;

import java.util.logging.Logger;

public class App 
{

    public static void main(String[] argv) {

        DataSource ds = new CSVDataSource();
        ds.start("EUR.USD")
                .compose(new SimpleBollingerStrategy("EUR.USD", 30, 1.5))
                .compose(new StrategyExecutor(1000000))
                .subscribe(execution -> {

                    //Print out closing balances

                    if (execution.getResult() == ExecutionResult.OK)
                    if (execution.getSignal().getSide() == Side.EXIT_LONG || execution.getSignal().getSide() == Side.EXIT_SHORT)
                    System.out.println("Current balance: " + execution.getBalance());

                });

    }
}
