package com.kushtrimh.tomorr.artist;

import com.kushtrimh.tomorr.artist.repository.ArtistRepository;
import com.kushtrimh.tomorr.dal.extension.TestDatabaseExtension;
import com.kushtrimh.tomorr.dal.tables.records.ArtistRecord;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith({SpringExtension.class, TestDatabaseExtension.class})
public class ArtistJooqRepositoryTest {

    private final ArtistRepository<ArtistRecord> artistRepository;

    @Autowired
    public ArtistJooqRepositoryTest(ArtistRepository<ArtistRecord> artistRepository) {
        this.artistRepository = artistRepository;
    }


}
