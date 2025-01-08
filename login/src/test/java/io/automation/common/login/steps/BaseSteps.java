package io.automation.common.login.steps;


import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.automation.common.page.BasePage;
import io.automation.common.driver.DriverHook;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

import static org.openqa.selenium.support.PageFactory.initElements;

public class BaseSteps {
    private static final Logger log = LoggerFactory.getLogger(BaseSteps.class);

    @Autowired
    private DriverHook hooks;

    @Autowired
    private List<BasePage> pages;

    @Before(order = 1)
    public void logBeforeScenario(final Scenario scenario) {
        log.debug(StringUtils.rightPad("Starting scenario:", 20) + "[{}] - [{}]",
                getFeatureName(scenario),
                scenario.getName());
    }

    @Before(order = 2)
    public void initializeDriver() {
        final WebDriver driver = hooks.getDriver();
        // Initialize all page elements
        pages.forEach(p -> {
            initElements(driver, p);
        });
    }

    @After(order = 1)
    public void closeDriver(final Scenario scenario) {
        hooks.tearDown(scenario);
        hooks.closeDriver();
    }

    @After(order = Integer.MAX_VALUE)
    public void logAfterScenario(final Scenario scenario) {
        log.debug(StringUtils.rightPad("Finished scenario:", 20) + "[{}] - [{}] [{}]",
                getFeatureName(scenario),
                scenario.getName(),
                scenario.getStatus());
    }

    private String getFeatureName(Scenario scenario) {
        String featureName = scenario.getId();
        featureName = StringUtils.substringBeforeLast(featureName, ".feature");
        featureName = StringUtils.substringAfterLast(featureName, "/");
        return featureName;
    }
}
