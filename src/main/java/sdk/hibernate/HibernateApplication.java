package sdk.hibernate;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
public class HibernateApplication {
    @Autowired
    private Environment environment;

//    public static void main(String[] args) {
//        SpringApplication.run(HibernateApplication.class, args);
//    }

    @Bean
    @Primary
    public DataSource getDataSource() {
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.driverClassName(environment.getProperty("connection.driverClassName"));
//        dataSourceBuilder.url(environment.getProperty("spring.datasource.url"));
//        dataSourceBuilder.username(environment.getProperty("spring.datasource.username"));
//        dataSourceBuilder.password(environment.getProperty("spring.datasource.password"));
//        return dataSourceBuilder.build();

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(environment.getProperty("spring.datasource.driver-class-name"));
        }
        catch (PropertyVetoException exc) {
            throw new RuntimeException(exc);
        }

        dataSource.setJdbcUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUser(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));
        // set connection pool props
        dataSource.setInitialPoolSize(Integer.parseInt(environment.getProperty("connection.pool.initialPoolSize")));
        dataSource.setMinPoolSize(Integer.parseInt(environment.getProperty("connection.pool.minPoolSize")));
        dataSource.setMaxPoolSize(Integer.parseInt(environment.getProperty("connection.pool.maxPoolSize")));
        dataSource.setMaxIdleTime(Integer.parseInt(environment.getProperty("connection.pool.maxIdleTime")));
        return dataSource;

//        HikariConfig config = new HikariConfig();
//        HikariDataSource ds;
//        config.setJdbcUrl( environment.getProperty("spring.datasource.url") );
//        config.setUsername( environment.getProperty("spring.datasource.username") );
//        config.setPassword( environment.getProperty("spring.datasource.password") );
//        config.addDataSourceProperty( "cachePrepStmts" , "true" );
//        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
//        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
//        //config.setConnectionTimeout(500);
//        config.setConnectionTimeout(Long.parseLong(environment.getProperty("spring.datasource.hikari.connectionTimeout")));
//        config.setIdleTimeout(Long.parseLong(environment.getProperty("spring.datasource.hikari.idleTimeout")));
//        config.setMaximumPoolSize( Integer.parseInt(environment.getProperty("connection.pool.initialPoolSize")));
//        config.setMaxLifetime(Long.parseLong(environment.getProperty("spring.datasource.hikari.maxLifetime")));
//        ds = new HikariDataSource( config );
//        return ds;
    }

    //
    @Bean(name = "sessionFactory")
    @Primary
    public LocalSessionFactoryBean getSessionFactory(DataSource dataSource) {            // creating session factory
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        //sessionFactory.setPackagesToScan(new String[]{" com.*"});
       sessionFactory.setPackagesToScan("sdk.POJO");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    private Properties hibernateProperties() {                  // configure hibernate properties
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.default_schema", environment.getRequiredProperty("hibernate.default_schema"));
        //properties.put("spring.jpa.generate-ddl", false);
        return properties;
    }

   @Autowired
    @Bean(name = "transactionManager")
    @Primary
     // creating transaction manager factory
    public HibernateTransactionManager getTransactionManager(
            SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(
                sessionFactory);
        return transactionManager;
    }
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendor = new HibernateJpaVendorAdapter();

        vendor.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendor);
        factory.setDataSource(getDataSource());
        //factory.setPackagesToScan(new String[]{" com.*"});
       factory.setPackagesToScan("sdk.POJO");
        factory.setPersistenceUnitName("name");
        return factory;

    }

}
