package ru.aegorova.friendslist;


import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "ru.aegorova.friendslist")
@EnableTransactionManagement
@PropertySources({@PropertySource("classpath:application.properties"),
        @PropertySource("classpath:db.properties")})
public class AuroraApplicationConfig {

    @Autowired
    private Environment environment;

    // data source bean
    @Bean("dataSource")
    DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(environment.getProperty("database.driver-class-name"))
                .url(environment.getProperty("database.url"))
                .username(environment.getProperty("database.username"))
                .password(environment.getProperty("database.password"))
                .build();
    }

    // data source transaction manager bean
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    // data source connection provider bean
    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider
                (new TransactionAwareDataSourceProxy(dataSource()));
    }

    // jooq configuration bean
    @Bean
    public DefaultConfiguration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(SQLDialect.POSTGRES);
        jooqConfiguration.set(connectionProvider());
        jooqConfiguration
                .set(new DefaultExecuteListenerProvider(new JooqExceptionTranslator()));
        return jooqConfiguration;
    }

    // dsl context bean
    @Bean
    public DefaultDSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }
}
