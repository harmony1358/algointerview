package com.algotrader.interview.data;

import io.reactivex.Flowable;

public interface DataSource {

    Flowable<Candle> start (String instrumentName);

}
