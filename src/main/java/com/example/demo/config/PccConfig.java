package com.example.demo.config;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.cache.config.EnableGemfireCaching;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@EnableGemfireCaching
@EnableEntityDefinedRegions(basePackages = "com.example.demo.model")
@EnableGemfireRepositories(basePackages = "com.example.demo.repo.gem")
@Profile("cloud")
@Configuration
public class PccConfig extends AbstractCloudConfig {
}
