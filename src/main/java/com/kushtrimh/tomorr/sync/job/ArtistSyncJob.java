package com.kushtrimh.tomorr.sync.job;

import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.limit.RequestLimitService;
import com.kushtrimh.tomorr.properties.LimitProperties;
import com.kushtrimh.tomorr.sync.produce.ArtistSyncTaskProducer;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Kushtrim Hajrizi
 */
public class ArtistSyncJob extends QuartzJobBean {
    private final Logger logger = LoggerFactory.getLogger(ArtistSyncJob.class);

    private final ArtistSyncTaskProducer artistSyncTaskProducer;
    private final LimitProperties limitProperties;
    private final RequestLimitService requestLimitService;

    public ArtistSyncJob(
            ArtistSyncTaskProducer artistSyncTaskProducer,
            LimitProperties limitProperties,
            RequestLimitService requestLimitService) {
        this.artistSyncTaskProducer = artistSyncTaskProducer;
        this.limitProperties = limitProperties;
        this.requestLimitService = requestLimitService;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        logger.debug("Artist sync job started...");

        long taskRelatedRequests = requestLimitService.getSentRequestsCounter(LimitType.SPOTIFY_SEARCH);
        long artistSearchRequests = requestLimitService.getSentRequestsCounter(LimitType.SPOTIFY_SEARCH);
        logger.info("{} sync related requests for Spotify sent, limit {}", taskRelatedRequests, limitProperties.getSpotifySync());
        logger.info("{} search related requests for Spotify sent, limit {}", artistSearchRequests, limitProperties.getSpotifySearch());

        requestLimitService.resetAll();
        logger.info("All request limits reset back to 0");

        artistSyncTaskProducer.produce();
    }
}
