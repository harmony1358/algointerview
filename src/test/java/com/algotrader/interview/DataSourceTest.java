package com.algotrader.interview;

import static junit.framework.TestCase.assertTrue;

public class DataSourceTest extends BaseTest {

    public void shouldLoadAndParseDataFromCSV () {

        ds.start("TEST_CASE_DS")
                .map(candle -> {

                    assertTrue(candle.getStamp() > 0);
                    return candle;

                })
                .test()
                .assertNoErrors();


    }


}
