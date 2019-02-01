package com.algotrader.interview.data;

import java.util.HashMap;

public class Studies {

    private Candle candle;
    private HashMap<String, Double> studies = new HashMap<>();

    public Studies(Candle candle) {
        this.candle = candle;
    }

    public Candle getCandle() {
        return candle;
    }

    public void setCandle(Candle candle) {
        this.candle = candle;
    }

    public HashMap<String, Double> getStudies() {
        return studies;
    }

    public void setStudies(HashMap<String, Double> studies) {
        this.studies = studies;
    }

    public void setStudyValue (String name, Double value) {
        this.studies.put(name, value);
    }

    public Double getStudyValue (String name) {
        return this.studies.get(name);
    }

}
