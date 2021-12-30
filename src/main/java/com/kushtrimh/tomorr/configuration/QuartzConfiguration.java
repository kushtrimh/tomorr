package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.spotify.job.SpotifyAuthenticationJob;
import com.kushtrimh.tomorr.sync.job.SyncJob;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
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

    // Authentication Job
    @Bean
    public JobDetailFactoryBean authenticationJobDetailFactoryBean() {
        return createJobDetailFactoryBean(SpotifyAuthenticationJob.class);
    }

    @Bean
    public SimpleTriggerFactoryBean authenticationTriggerFactoryBean(
            @Qualifier("authenticationJobDetailFactoryBean") JobDetailFactoryBean jobDetailFactoryBean) {
        return createInfiniteSimpleTriggerFactoryBean(jobDetailFactoryBean, 3240000);
    }

    // Artist Sync Job
    @Bean
    public JobDetailFactoryBean artistSyncJobDetailFactoryBean() {
        return createJobDetailFactoryBean(SyncJob.class);
    }

    @Bean
    public SimpleTriggerFactoryBean artistSyncTriggerFactoryBean(
            @Qualifier("artistSyncJobDetailFactoryBean") JobDetailFactoryBean jobDetailFactoryBean) {
        return createInfiniteSimpleTriggerFactoryBean(jobDetailFactoryBean, 60000);
    }

    // Job details and triggers
    @Bean
    public JobDetail[] jobDetails(
            @Qualifier("authenticationJobDetailFactoryBean") JobDetailFactoryBean authenticationJobDetailFactoryBean,
            @Qualifier("artistSyncJobDetailFactoryBean") JobDetailFactoryBean artistSyncJobDetailFactoryBean) {
        return new JobDetail[]{
                authenticationJobDetailFactoryBean.getObject(),
                artistSyncJobDetailFactoryBean.getObject()
        };
    }

    @Bean
    public Trigger[] triggers(
            @Qualifier("authenticationTriggerFactoryBean") SimpleTriggerFactoryBean authenticationTriggerFactoryBean,
            @Qualifier("artistSyncTriggerFactoryBean") SimpleTriggerFactoryBean artistSyncTriggerFactoryBean) {
        return new Trigger[]{
                authenticationTriggerFactoryBean.getObject(),
                artistSyncTriggerFactoryBean.getObject()
        };
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource,
                                                     ApplicationContext applicationContext,
                                                     JobDetail[] jobDetails,
                                                     Trigger[] triggers) {
        var springBeanJobFactory = new SpringBeanJobFactory();
        springBeanJobFactory.setApplicationContext(applicationContext);

        var properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        var schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setQuartzProperties(properties);
        schedulerFactoryBean.setJobFactory(springBeanJobFactory);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setJobDetails(jobDetails);
        schedulerFactoryBean.setTriggers(triggers);
        return schedulerFactoryBean;
    }


    private JobDetailFactoryBean createJobDetailFactoryBean(Class<? extends QuartzJobBean> cls) {
        var jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(cls);
        jobDetailFactoryBean.setDurability(true);
        return jobDetailFactoryBean;
    }

    private SimpleTriggerFactoryBean createInfiniteSimpleTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean,
                                                                            long repeatInterval) {
        var simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
        simpleTriggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
        simpleTriggerFactoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        simpleTriggerFactoryBean.setRepeatInterval(repeatInterval);
        return simpleTriggerFactoryBean;
    }
}
