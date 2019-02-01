package com.algotrader.interview;

import org.junit.Test;

public class AppTest extends BaseTest {

    @Test(expected = Test.None.class)
    public void shouldRunAppWithoutAnyErrors () {

        App.main(new String[0]);

    }

}
