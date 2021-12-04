package com.kushtrimh.tomorr.configuration;

import com.kushtrimh.tomorr.album.repository.AlbumJooqRepository;
import com.kushtrimh.tomorr.album.repository.AlbumRepository;
import com.kushtrimh.tomorr.artist.repository.ArtistJooqRepository;
import com.kushtrimh.tomorr.artist.repository.ArtistRepository;
import com.kushtrimh.tomorr.dal.tables.records.AlbumRecord;
import com.kushtrimh.tomorr.dal.tables.records.AppUserRecord;
import com.kushtrimh.tomorr.dal.tables.records.ArtistRecord;
import com.kushtrimh.tomorr.extension.TestDatabaseExtension;
import com.kushtrimh.tomorr.properties.DataSourceProperties;
import com.kushtrimh.tomorr.user.repository.UserJooqRepository;
import com.kushtrimh.tomorr.user.repository.UserRepository;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;

/**
 * @author Kushtrim Hajrizi
 */
@Configuration
@TestPropertySource(inheritProperties = false, inheritLocations = false)
@Import({DataAccessConfiguration.class})
public class TestDataSourceConfiguration {

    @Bean
    public DataSourceProperties dataSourceProperties() {
        var dataSourceProperties = new DataSourceProperties();
        GenericContainer<?> container = TestDatabaseExtension.getPostgreSQLContainer();
        dataSourceProperties.setUrl(String.format("jdbc:postgresql://%s:%d/tomorrtest",
                container.getHost(), container.getFirstMappedPort()));
        dataSourceProperties.setUsername("postgres");
        dataSourceProperties.setPassword("postgres");
        return dataSourceProperties;
    }

    @Bean
    public AlbumRepository<AlbumRecord> albumRepository(DSLContext dsl) {
        return new AlbumJooqRepository(dsl);
    }

    @Bean
    public ArtistRepository<ArtistRecord> artistRepository(DSLContext dsl) {
        return new ArtistJooqRepository(dsl);
    }

    @Bean
    public UserRepository<AppUserRecord> userRepository(DSLContext dsl) {
        return new UserJooqRepository(dsl);
    }
}
