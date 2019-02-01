package com.algotrader.interview;

import org.junit.Test;

public class DataSourceTest extends BaseTest {

    @Test
    public void shouldLoadAndParseDataFromCSV () {


        ds.start("TEST_CASE_DS")
                .test()
                .assertNoErrors();


    }


}
