package com.algotrader.interview;

import com.algotrader.interview.strategy.StudyEnvelope;
import com.algotrader.interview.studies.BollingerBands;
import com.algotrader.interview.studies.MA;
import com.algotrader.interview.studies.StdDev;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class StudiesTest extends BaseTest {

    @Test
    public void shouldProperlyCalculateMA () {

        ds.start("TEST_CASE_MA")
                .map(StudyEnvelope::new)
                .compose(new MA("MA", "CLOSE", 10))
                .map(studyEnvelope  -> {


                    assertEquals(0, Double.compare(
                            studyEnvelope.getStudyValue("OPEN"),
                            studyEnvelope.getStudyValue("MA")
                    ));

                    return studyEnvelope;

                })
                .test()
                .assertNoErrors();

    }

    @Test
    public void shouldProperlyCalculateStdDev () {

        ds.start("TEST_CASE_SD")
                .map(StudyEnvelope::new)
                .compose(new StdDev("SD", "CLOSE", 10))
                .map(studyEnvelope  -> {

                    assertEquals(0, Double.compare(
                            studyEnvelope.getStudyValue("OPEN"),
                            studyEnvelope.getStudyValue("SD")
                    ));

                    return studyEnvelope;

                })
                .test()
                .assertNoErrors();

    }

    @Test
    public void shouldProperlyCalculateBBands () {

        ds.start("TEST_CASE_BB")
                .map(StudyEnvelope::new)
                .compose(new BollingerBands("BB", "CLOSE", 10, 1.5))
                .map(studyEnvelope  -> {

                    assertEquals(0, Double.compare(
                            studyEnvelope.getStudyValue("OPEN"),
                            studyEnvelope.getStudyValue("BB_UPPER")
                    ));

                    assertEquals(0, Double.compare(
                            studyEnvelope.getStudyValue("LOW"),
                            studyEnvelope.getStudyValue("BB_MIDDLE")
                    ));

                    assertEquals(0, Double.compare(
                            studyEnvelope.getStudyValue("HIGH"),
                            studyEnvelope.getStudyValue("BB_LOWER")
                    ));

                    return studyEnvelope;

                })
                .test()
                .assertNoErrors();

    }

}
