package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.PooledServiceConnectorConfig;
import org.springframework.cloud.service.relational.DataSourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.demo.repo.jpa")
@Profile("cloud")
public class DbCloudConfig extends AbstractCloudConfig {

    @Bean
    public DataSource dataSource() {
        DataSource dataSource = connectionFactory().dataSource();

        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("demo.sql"));

        DatabasePopulatorUtils.execute(databasePopulator, dataSource);

//        PooledServiceConnectorConfig.PoolConfig poolConfig = new PooledServiceConnectorConfig.PoolConfig(
//                5 /* min */, 30 /* max */, 3000 /* maxWait */);
//        return connectionFactory().dataSource(new DataSourceConfig(poolConfig, null));

        return dataSource;
    }

}