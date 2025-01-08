package io.automation.common.runners;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest
public class SpringBootRunner {

    @Before
    public void setupCucumberSpringContext() {

    }
}
