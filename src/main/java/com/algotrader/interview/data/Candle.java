package com.algotrader.interview.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Candle {

    private Long stamp;
    private Double open;
    private Double high;
    private Double low;
    private Double close;

    public Candle(Long stamp, Double open, Double high, Double low, Double close) {
        this.stamp = stamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

    public Long getStamp() {
        return stamp;
    }

    public void setStamp(Long stamp) {
        this.stamp = stamp;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    @Override
    public String toString() {

        return "[stamp=" + this.stamp +
                ", open=" + this.open +
                ", high=" + this.high +
                ", low=" + this.low +
                ", close=" +this.close+ "]";

    }

    public static Candle parse (String string) throws ParseException {

        String[] columns = string.split(",");

        Long stamp = new SimpleDateFormat("dd.MM.yyyy").parse(columns[0]).getTime();
        Double open  = Double.parseDouble(columns[1]);
        Double low   = Double.parseDouble(columns[2]);
        Double high  = Double.parseDouble(columns[3]);
        Double close = Double.parseDouble(columns[4]);

        return new Candle(stamp, open, high, low, close);
    }
}
