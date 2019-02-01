package com.algotrader.interview.strategy;

import com.algotrader.interview.data.Candle;

import java.util.HashMap;

public class StudyEnvelope {

    private Long stamp;
    private HashMap<String, Double> studies = new HashMap<>();

    public StudyEnvelope(Candle candle) {

        this.stamp = candle.getStamp();
        setStudyValue("OPEN", candle.getOpen());
        setStudyValue("HIGH", candle.getHigh());
        setStudyValue("LOW", candle.getLow());
        setStudyValue("CLOSE", candle.getClose());

    }

    public Long getStamp() {
        return stamp;
    }

    public void setStamp(Long stamp) {
        this.stamp = stamp;
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
