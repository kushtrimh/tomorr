package com.kushtrimh.tomorr.artist.service;

import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.artist.cache.ArtistCache;
import com.kushtrimh.tomorr.artist.cache.ArtistRedisCache;
import com.kushtrimh.tomorr.configuration.TestRedisConfiguration;
import com.kushtrimh.tomorr.extension.TestRedisExtension;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kushtrim Hajrizi
 */
@Tags({@Tag("integration"), @Tag("redis")})
@ContextConfiguration(classes = {TestRedisConfiguration.class, DefaultArtistSearchServiceIntegrationTest.ArtistSearchConfiguration.class})
@ExtendWith({SpringExtension.class, TestRedisExtension.class})
class DefaultArtistSearchServiceIntegrationTest {

    private static final List<Artist> artists = List.of(
            new Artist("artist1", "artist one", "http://localhost:13251/artist/1", 54),
            new Artist("artist2", "artist two", "http://localhost:13251/artist/2", 85)
    );

    @Autowired
    private ArtistSearchService artistSearchService;

    @Test
    public void search_WhenSameArtistsAreSearchedTwice_ReturnCachedArtists() {
        var name = "artist";
        assertAll(
                () -> assertEquals(artists, artistSearchService.search(name, false)),
                () -> assertEquals(artists, artistSearchService.search(name, false))
        );
    }

    @Configuration
    @EnableCaching
    public static class ArtistSearchConfiguration {

        @Bean
        public ArtistCache artistCache(StringRedisTemplate stringRedisTemplate) {
            return new ArtistRedisCache(stringRedisTemplate);
        }

        public ArtistService artistService() {
            return new ArtistService() {
                @Override
                public Optional<Artist> findById(String id) {
                    return Optional.empty();
                }

                @Override
                public Optional<Artist> findByName(String name) {
                    return Optional.empty();
                }

                @Override
                public List<Artist> searchByName(String name) {
                    return artists;
                }

                @Override
                public List<Artist> findToSync(String syncKey, int count) {
                    return null;
                }

                @Override
                public void save(Artist artist) {

                }

                @Override
                public void deleteById(String id) {

                }

                @Override
                public boolean exists(String id) {
                    return false;
                }
            };
        }

        @Bean
        public ArtistSearchService artistSearchService(
                ArtistCache artistCache) {
            return new DefaultArtistSearchService(
                    artistCache,
                    artistService(),
                    null);
        }
    }
}