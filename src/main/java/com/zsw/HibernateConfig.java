package com.zsw;


import com.alibaba.druid.pool.DruidDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * Created by zhangshaowei on 2020/4/14.
 */


@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.zsw" })
//@PropertySource(value = { "classpath:application.yml" })
public class HibernateConfig {

    @Autowired
    DruidDataSource druidDataSource;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(druidDataSource);
        sessionFactory.setPackagesToScan(new String[] { "com.zsw.entitys" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    //获取hibernate配置
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL5InnoDBDialect");
        properties.setProperty("hibernate.show_sql","true");
        properties.setProperty("hibernate.format_sql","true");
        properties.setProperty("hibernate.hbm2ddl.auto","update");
        properties.setProperty("hibernate.cache.use_second_level_cache","false");
        properties.setProperty("hibernate.cache.use_query_cache","false");
        properties.setProperty("hibernate.jdbc.fetch_size","50");
        properties.setProperty("hibernate.connection.autocommit","false");
        properties.setProperty("hibernate.connection.release_mode","auto");
        //properties.setProperty("hibernate.current_session_context_class","org.springframework.orm.hibernate4.SpringSessionContext");
        properties.setProperty("hibernate.current_session_context_class","org.springframework.orm.hibernate5.SpringSessionContext");
        properties.setProperty("javax.persistence.validation.mode","none");
        return properties;
    }
    // 事务管理
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sf) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sf);
        return txManager;
    }
}