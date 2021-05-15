package com.kushtrimh.tomorr.spotify.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Kushtrim Hajrizi
 */

public class SpotifyAuthenticationJob extends QuartzJobBean {

    private final Logger logger = LoggerFactory.getLogger(SpotifyAuthenticationJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    }
}
