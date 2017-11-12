package com.unit1.testRunner;

/**
 * Created by Unit 1 on 02-Jul-17.
 */

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * @author vthipperudrappa
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"com.unit1.testhooks",
                "com.unit1.stepDefs"
        },
        features = {"src/test/resources/features/"},
        plugin = {
                "pretty", "html:target/cucumber-reports",
                "json:target/cucumber-reports/cucumber.json"},
        tags = {"@Rest"}
)
public class RunTest {
}
