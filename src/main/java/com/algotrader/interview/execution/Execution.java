package com.algotrader.interview.execution;

import com.algotrader.interview.strategy.Signal;

public class Execution {

    private Long stamp;
    private Signal signal;
    private Double balance;
    private ExecutionResult result;

    public Execution(Long stamp, Signal signal, Double balance, ExecutionResult result) {
        this.stamp = stamp;
        this.signal = signal;
        this.balance = balance;
        this.result = result;
    }

    public Long getStamp() {
        return stamp;
    }

    public void setStamp(Long stamp) {
        this.stamp = stamp;
    }

    public Signal getSignal() {
        return signal;
    }

    public void setSignal(Signal signal) {
        this.signal = signal;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public ExecutionResult getResult() {
        return result;
    }

    public void setResult(ExecutionResult result) {
        this.result = result;
    }
}
