package com.algotrader.interview;

import com.algotrader.interview.data.CSVDataSource;
import com.algotrader.interview.data.DataSource;
import com.algotrader.interview.strategy.Signal;
import com.algotrader.interview.strategy.StudyEnvelope;
import com.algotrader.interview.studies.MA;
import io.reactivex.FlowableTransformer;

import static junit.framework.TestCase.assertTrue;

public class BaseTest {

    protected final DataSource ds = new CSVDataSource();

    protected void testStudy (String instrument, FlowableTransformer<StudyEnvelope, StudyEnvelope> study) {

        ds.start(instrument)
                .map(StudyEnvelope::new)
                .compose(new MA("MA", "CLOSE", 10))
                .map(studyEnvelope  -> {

                    assertTrue(true); // Fill Values in test case
                    return studyEnvelope;

                })
                .test()
                .assertNoErrors();

    }

    protected  void testStrategy (String instrument, FlowableTransformer<StudyEnvelope, Signal> strategy) {

        ds.start(instrument)
                .map(StudyEnvelope::new)
                .compose(strategy)
                .map(signal -> {
                    assertTrue(true);
                    return signal;
                })
                .test()
                .assertNoErrors();

    }
}
