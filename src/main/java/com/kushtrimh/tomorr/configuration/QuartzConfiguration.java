package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.spotify.job.SpotifyAuthenticationJob;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
public class QuartzConfiguration {

    private final QuartzProperties quartzProperties;

    public QuartzConfiguration(QuartzProperties quartzProperties) {
        this.quartzProperties = quartzProperties;
    }

    public JobDetailFactoryBean authenticationJobDetailFactoryBean() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(SpotifyAuthenticationJob.class);
        jobDetailFactoryBean.setDurability(true);
        return jobDetailFactoryBean;
    }

    public SimpleTriggerFactoryBean authenticationTriggerFactoryBean() {
        // TODO:
        return null;
    }

    @Bean
    public JobDetail[] jobDetails(
            @Qualifier("authenticationJobDetailFactoryBean") JobDetailFactoryBean authenticationJobDetailFactoryBean) {
        return new JobDetail[] {
                authenticationJobDetailFactoryBean.getObject()
        };
    }

    @Bean
    public Trigger[] triggers(
            @Qualifier("authenticationTriggerFactoryBean") SimpleTriggerFactoryBean authenticationTriggerFactoryBean) {
        return new Trigger[] {
                authenticationTriggerFactoryBean.getObject()
        };
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource,
                                                     ApplicationContext applicationContext,
                                                     JobDetail[] jobDetails,
                                                     Trigger[] triggers) {
        SpringBeanJobFactory springBeanJobFactory = new SpringBeanJobFactory();
        springBeanJobFactory.setApplicationContext(applicationContext);

        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setQuartzProperties(properties);
        schedulerFactoryBean.setJobFactory(springBeanJobFactory);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setJobDetails(jobDetails);
        schedulerFactoryBean.setTriggers(triggers);
        return schedulerFactoryBean;
    }
}
