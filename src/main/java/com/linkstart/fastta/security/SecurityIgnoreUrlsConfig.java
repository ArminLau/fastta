package com.linkstart.fastta.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.ignored")
public class SecurityIgnoreUrlsConfig {
    private List<String> urls;
}
