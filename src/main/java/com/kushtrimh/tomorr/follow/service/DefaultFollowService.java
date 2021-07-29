package com.kushtrimh.tomorr.follow.service;

import com.kushtrimh.tomorr.artist.cache.ArtistCache;
import com.kushtrimh.tomorr.artist.service.ArtistSearchService;
import com.kushtrimh.tomorr.artist.service.ArtistService;
import com.kushtrimh.tomorr.user.User;
import com.kushtrimh.tomorr.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultFollowService implements FollowService {
    private final Logger logger = LoggerFactory.getLogger(DefaultFollowService.class);

    private final UserService userService;
    private final ArtistService artistService;
    private final ArtistCache artistCache;
    private final ArtistSearchService artistSearchService;

    public DefaultFollowService(UserService userService,
                                ArtistService artistService,
                                ArtistCache artistCache,
                                ArtistSearchService artistSearchService) {
        this.userService = userService;
        this.artistService = artistService;
        this.artistCache = artistCache;
        this.artistSearchService = artistSearchService;
    }

    @Transactional
    @Override
    public boolean follow(User user, String artistId) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(artistId);
        // Check if artist id exists anywhere on cache -> database -> Spotify.
        // If it does not exist at any of those places, it means artist id is probably invalid
        if (artistCache.containsArtistId(artistId) || artistService.exists(artistId) || artistSearchService.exists(artistId)) {
            userService.associate(user, artistId);
            return true;
        }
        return false;
    }
}
