package com.kushtrimh.tomorr.sync.job;

import com.kushtrimh.tomorr.limit.RequestLimitService;
import com.kushtrimh.tomorr.properties.LimitProperties;
import com.kushtrimh.tomorr.sync.produce.ArtistSyncTaskProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith(MockitoExtension.class)
class SyncJobTest {

    @Mock
    private ArtistSyncTaskProducer artistSyncTaskProducer;
    @Mock
    private LimitProperties limitProperties;
    @Mock
    private RequestLimitService requestLimitService;

    private SyncJob syncJob;

    @BeforeEach
    public void init() {
        syncJob = new SyncJob(artistSyncTaskProducer, limitProperties, requestLimitService);
    }

    @Test
    public void executeInternal_WhenJobIsExecuted_ResetLimitsAndStartTaskProducer() throws JobExecutionException {
        JobExecutionContext context = mock(JobExecutionContext.class);
        syncJob.executeInternal(context);
        verify(requestLimitService, times(1)).resetAll();
        verify(artistSyncTaskProducer, times(1)).produce();
    }
}