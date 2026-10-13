// NECESSÁRIA IMPORTAÇÃO DE BIBLIOTECAS JUNIT E HAMCREST

package edu.senacsp.health_management;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(TestSuite.class)
@Suite.SuiteClasses({ UserTest.class, ProfileTest.class })

public class TestSuite {
}
