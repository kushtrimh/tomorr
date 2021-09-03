package com.kushtrimh.tomorr.task.job;

import com.kushtrimh.tomorr.artist.service.ArtistService;
import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.limit.RequestLimitService;
import com.kushtrimh.tomorr.properties.LimitProperties;
import com.kushtrimh.tomorr.task.data.ArtistTaskData;
import com.kushtrimh.tomorr.task.manager.TaskManager;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author Kushtrim Hajrizi
 */
public class ArtistSyncTaskCreatorJob extends QuartzJobBean {
    private final Logger logger = LoggerFactory.getLogger(ArtistSyncTaskCreatorJob.class);

    private final TaskManager<ArtistTaskData> artistSyncTaskManager;
    private final RequestLimitService requestLimitService;
    private final ArtistService artistService;
    private final LimitProperties limitProperties;

    public ArtistSyncTaskCreatorJob(TaskManager<ArtistTaskData> artistSyncTaskManager,
                                    RequestLimitService requestLimitService,
                                    ArtistService artistService,
                                    LimitProperties limitProperties) {
        this.artistSyncTaskManager = artistSyncTaskManager;
        this.requestLimitService = requestLimitService;
        this.artistService = artistService;
        this.limitProperties = limitProperties;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Artist sync job started...");

        long taskRelatedRequests = requestLimitService.getSentRequestsCounter(LimitType.ARTIST_SEARCH);
        long artistSearchRequests = requestLimitService.getSentRequestsCounter(LimitType.ARTIST_SEARCH);
        // TODO: Change to getTaskRequestLimit
        logger.info("{} task related requests sent, limit {}", taskRelatedRequests, limitProperties.getSpotify());
        logger.info("{} artist search related requests sent, limit {}", artistSearchRequests, limitProperties.getArtistSearch());

        requestLimitService.reset(LimitType.ALL);
        logger.info("All request limits reset back to 0");

        long tasksInQueue = artistSyncTaskManager.getQueuedTasksCount();
        logger.info("{} tasks currently in queue", tasksInQueue);

        long tasksToCreate = (long) limitProperties.getSpotify() - tasksInQueue;
        logger.info("{} tasks will be created", tasksToCreate);
        // Get artists from tasks to create, based on the number of tasksToCreate, update their sync_key as well
        // If no artists returned, update the sync key on the database as well
        // For returned artists create tasks with SYNC type


    }
}
