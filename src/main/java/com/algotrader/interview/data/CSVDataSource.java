package com.algotrader.interview.data;

import io.reactivex.Flowable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

public class CSVDataSource implements DataSource {

    private static final Logger LOG = LogManager.getLogger(CSVDataSource.class);
    @Override
    public Flowable<Candle> start(String instrumentName) {

        URL url = getClass().getClassLoader().getResource(instrumentName+".csv");

        if (url == null) {

            LOG.debug("Resource: " + instrumentName + ".csv" + " not found!");
            return Flowable.never();

        }

        File file = new File(url.getFile());

        LOG.debug("Reading from file: " + file.getAbsolutePath());

        return Flowable.using(

                    () -> new BufferedReader( new FileReader(file)),
                                            reader -> Flowable.fromIterable(() -> reader.lines().iterator()),
                    BufferedReader::close

                )
                .skip(1)
                .map(Candle::parse);

    }

}
