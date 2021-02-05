package org.openpaas.paasta.portal.common.api.config.dataSource;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by indra on 2018-02-07.
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "org.openpaas.paasta.portal.common.api.repository.uaa",
        entityManagerFactoryRef = "uaaEntityManager",
        transactionManagerRef = "uaaTransactionManager"
)
public class UaaConfig {

    private static final Logger logger = getLogger(UaaConfig.class);

    @Value("${datasource.hikari.maximum-pool-size}") int maximumPoolSize;
    @Value("${datasource.uaa.driver-class-name}") String uaaDriverClassName;
    @Value("${datasource.uaa.jdbc-url}") String uaaUrl;
    @Value("${datasource.uaa.username}") String uaaUsername;
    @Value("${datasource.uaa.password}") String uaaPassword;
    @Value("${jpa.hibernate.ddl-auto}") String ddlAuto;
    @Value("${jpa.hibernate.naming.strategy}") String dialect;


    @Bean
    public boolean loggerPrintConfigC() {

        logger.info("[UaaConfig]=======================================================================");
        logger.info(uaaDriverClassName+" <= uaaDriverClassName");
        logger.info(uaaUrl+" <= uaaUrl");
        logger.info(uaaUsername+" <= uaaUsername");
        logger.info(uaaPassword+" <= uaaPassword");
        logger.info(ddlAuto+" <= UaaAuto");
        logger.info(dialect+" <= Uaadialect");
        logger.info(maximumPoolSize+" <= maximumPoolSize");
        logger.info("==================================================================================");

        return true;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean uaaEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(uaaDataSource());
        em.setPackagesToScan(new String[] { "org.openpaas.paasta.portal.common.api.entity.uaa" });

        HibernateJpaVendorAdapter vendorAdapter= new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",ddlAuto);
        properties.put("hibernate.dialect",dialect);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public HikariDataSource uaaDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setMaximumPoolSize(maximumPoolSize);
        hikariDataSource.setDriverClassName(uaaDriverClassName);
        hikariDataSource.setJdbcUrl(uaaUrl);
        hikariDataSource.setUsername(uaaUsername);
        hikariDataSource.setPassword(uaaPassword);

        return hikariDataSource;
    }

    @Bean
    public PlatformTransactionManager uaaTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(uaaEntityManager().getObject());
        return transactionManager;
    }
}
