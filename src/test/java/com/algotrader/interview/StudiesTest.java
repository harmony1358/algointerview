package com.algotrader.interview;

import com.algotrader.interview.studies.MA;
import com.algotrader.interview.studies.StdDev;
import org.junit.Test;

public class StudiesTest extends BaseTest {

    @Test
    public void shouldProperlyCalculateMA () {

        testStudy("TEST_CASE_MA", new MA("MA", "CLOSE", 10));

    }

    @Test
    public void shouldProperlyCalculateStdDev () {

        testStudy("TEST_CASE_SD", new StdDev("SD", "CLOSE", 10));

    }

    @Test
    public void shouldProperlyCalculateBBands () {

        testStudy("TEST_CASE_BB", new StdDev("BB", "CLOSE", 10));

    }

}
