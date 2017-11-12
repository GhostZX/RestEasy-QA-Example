package com.unit1.testhooks;

import com.google.inject.Inject;
import com.unit1.context.Context;
import cucumber.api.Scenario;
import cucumber.api.java.Before;

/**
 * Created by Unit 1 on 12-Nov-17.
 */
public class TestHooks {

    private Context context;

    @Inject
    public TestHooks(final Context context) {
        this.context = context;
    }

    @Before
    public void before(Scenario scenario) {
        context.setScenario(scenario);
    }

}
