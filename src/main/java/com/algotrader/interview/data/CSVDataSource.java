package com.algotrader.interview.data;

import io.reactivex.Flowable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CSVDataSource implements DataSource {

    @Override
    public Flowable<Candle> start(String instrumentName) {

        File file = new File(getClass().getClassLoader().getResource(instrumentName+".csv").getFile());
        return Flowable.using(
                () -> new BufferedReader(new FileReader(file)),
                reader -> Flowable.fromIterable(() -> reader.lines().iterator()),
                BufferedReader::close
        )
                .skip(1)
                .map(Candle::parse);

    }

}
