package edu.senacsp.health_management;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        UserTest.class,
        ProfileTest.class,
        RestrictionTest.class,
        ScheduleTest.class
})
public class TestSuite {
}
