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
        basePackages = "org.openpaas.paasta.portal.common.api.repository.cc",
        entityManagerFactoryRef = "ccEntityManager",
        transactionManagerRef = "ccTransactionManager"
)
public class CcConfig {

    private static final Logger logger = getLogger(CcConfig.class);

    @Value("${datasource.hikari.maximum-pool-size}") int maximumPoolSize;
    @Value("${datasource.cc.driver-class-name}") String ccDriverClassName;
    @Value("${datasource.cc.jdbc-url}") String ccUrl;
    @Value("${datasource.cc.username}") String ccUsername;
    @Value("${datasource.cc.password}") String ccPassword;
    @Value("${jpa.hibernate.ddl-auto}") String ddlAuto;
    @Value("${jpa.hibernate.naming.strategy}") String dialect;


    @Bean
    public boolean loggerPrintConfigB() {

        logger.info("[CcConfig]=======================================================================");
        logger.info(ccDriverClassName+" <= ccDriverClassName");
        logger.info(ccUrl+" <= ccUrl");
        logger.info(ccUsername+" <= ccUsername");
        logger.info(ccPassword+" <= ccPassword");
        logger.info(ddlAuto+" <= CcddlAuto");
        logger.info(dialect+" <= Ccdialect");
        logger.info(maximumPoolSize+" <= maximumPoolSize");
        logger.info("==================================================================================");
        return true;
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean ccEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(ccDataSource());
        em.setPackagesToScan(new String[] { "org.openpaas.paasta.portal.common.api.entity.cc" });

        HibernateJpaVendorAdapter vendorAdapter= new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",ddlAuto);
        properties.put("hibernate.dialect",dialect);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public HikariDataSource ccDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setMaximumPoolSize(maximumPoolSize);
        hikariDataSource.setDriverClassName(ccDriverClassName);
        hikariDataSource.setJdbcUrl(ccUrl);
        hikariDataSource.setUsername(ccUsername);
        hikariDataSource.setPassword(ccPassword);

        return hikariDataSource;
    }

    @Bean
    public PlatformTransactionManager ccTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(ccEntityManager().getObject());
        return transactionManager;
    }
}
