package io.automation.common.handler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "selenium")
public class CommonProperties {

    private String browser;

    private Boolean headlessBrowser;

    private String remoteBrowser;

    private String targetUrl;
}
