package com.recaptube.recaptube.storage.config;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
@MapperScan(basePackages = {
        "com.recaptube.recaptube.storage.core.mybatis"
}, sqlSessionTemplateRef = "sqlSessionTemplate")

@EnableJpaRepositories(
        entityManagerFactoryRef = "jpaEntityManagerFactory",
        transactionManagerRef = "jpaTransactionManager",
        basePackages = {
                "com.recaptube.recaptube.core.domain.youtube"
        }
)
public class DatabaseConfig {
    final private String MYBATIS_MYSQL_MAPPER_FILE_PATH = "classpath:mybatis/mapper/*.xml";

    private final String USERNAME = "sa";
    private final String PASSWORD = "";


    @Bean
    @Primary
    public DataSource dataSource(){
        return DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:tcp://localhost/~/test")
               .username(USERNAME)
               .password(PASSWORD)
               .build();
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setVfs(SpringBootVFS.class);
        sessionFactory.setConfigurationProperties(properties());
        sessionFactory.setMapperLocations(applicationContext.getResources(MYBATIS_MYSQL_MAPPER_FILE_PATH));
        sessionFactory.setTypeAliasesPackage("com.recaptube.recaptube");
        return sessionFactory.getObject();
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
        return sqlSessionTemplate;
    }

    @Bean(name = "sqlSession")
    public SqlSession sqlSession(SqlSessionFactory sqlSessionFactory) {
        final SqlSession sqlSession = sqlSessionTemplate(sqlSessionFactory);
        return sqlSession;
    }

//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        return new DataSourceTransactionManager(dataSource());
//    }

    @Primary
    @Bean( name = "jpaEntityManagerFactory" )
    public LocalContainerEntityManagerFactoryBean jpaEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource()).packages("com.recaptube.recaptube.storage").build();
    }

    @Primary
    @Bean(name = "jpaTransactionManager")
    public JpaTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean mfBean) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory( mfBean.getObject() );
        return transactionManager;
    }

    private Properties properties() {
        final Properties properties = new Properties();
        properties.setProperty("defaultExecutorType", "BATCH");
        properties.setProperty("cacheEnabled", "true");
        properties.setProperty("lazyLoadingEnabled", "true");
        properties.setProperty("useGeneratedKeys", "true");
        properties.setProperty("mapUnderscoreToCamelCase", "true");
        properties.setProperty("defaultFetchSize", "1000");
        return properties;
    }

}

