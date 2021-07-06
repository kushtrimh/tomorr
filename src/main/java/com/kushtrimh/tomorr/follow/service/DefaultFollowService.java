package com.kushtrimh.tomorr.follow.service;

import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.artist.service.ArtistService;
import com.kushtrimh.tomorr.spotify.api.SpotifyApiClient;
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
    private final SpotifyApiClient spotifyApiClient;

    public DefaultFollowService(UserService userService,
                                ArtistService artistService,
                                SpotifyApiClient spotifyApiClient) {
        this.userService = userService;
        this.artistService = artistService;
        this.spotifyApiClient = spotifyApiClient;
    }

    @Transactional
    @Override
    public void follow(User user, String artistId) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(artistId);
        if (!artistService.exists(artistId)) {
            // TODO: search for artist on spotify
        }
        userService.associate(user, artistId);
    }
}
