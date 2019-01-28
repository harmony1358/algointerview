package com.algotrader.interview.strategy;

public class Signal {

    private String  instrument;
    private Long    stamp;
    private Side    side;
    private Double  price;

    public Signal() {}

    public Signal(String instrument, Long stamp, Side side, Double price) {
        this.instrument = instrument;
        this.stamp = stamp;
        this.side = side;
        this.price = price;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public Long getStamp() {
        return stamp;
    }

    public void setStamp(Long stamp) {
        this.stamp = stamp;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return this.instrument + " -> " + this.side.name();
    }
}
