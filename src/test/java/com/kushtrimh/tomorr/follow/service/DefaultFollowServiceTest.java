package com.kushtrimh.tomorr.follow.service;

import com.kushtrimh.tomorr.artist.cache.ArtistCache;
import com.kushtrimh.tomorr.artist.service.ArtistSearchService;
import com.kushtrimh.tomorr.artist.service.ArtistService;
import com.kushtrimh.tomorr.user.User;
import com.kushtrimh.tomorr.user.UserType;
import com.kushtrimh.tomorr.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith({MockitoExtension.class})
class DefaultFollowServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private ArtistService artistService;
    @Mock
    private ArtistCache artistCache;
    @Mock
    private ArtistSearchService artistSearchService;

    private DefaultFollowService defaultFollowService;

    @BeforeEach
    public void init() {
        defaultFollowService = new DefaultFollowService(userService, artistService, artistCache, artistSearchService);
    }

    @Test
    public void follow_WhenUserOrArtistIdAreNull_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> defaultFollowService.follow(null, "artist1"));
        assertThrows(NullPointerException.class, () -> defaultFollowService.follow(
                new User("artist1@tomorr.com", UserType.EMAIL), null));
    }

    @Test
    public void follow_WhenArtistIsInCache_AssociateAndReturnTrue() {
        var user = new User("artist1@tomorr.com", UserType.EMAIL);
        var artistId = "artist1";
        when(artistCache.containsArtistId(artistId)).thenReturn(true);
        assertTrue(defaultFollowService.follow(user, artistId));
        verify(userService, times(1)).associate(user, artistId);
    }

    @Test
    public void follow_WhenArtistIsNotInCacheButExistsInDatabase_AssociateAndReturnTrue() {
        var user = new User("artist1@tomorr.com", UserType.EMAIL);
        var artistId = "artist1";
        when(artistCache.containsArtistId(artistId)).thenReturn(false);
        when(artistService.exists(artistId)).thenReturn(true);
        assertTrue(defaultFollowService.follow(user, artistId));
        verify(userService, times(1)).associate(user, artistId);
    }

    @Test
    public void follow_WhenArtistIsNotInCacheAndDatabaseButExistsInSpotify_AssociateAndReturnTrue() {
        var user = new User("artist1@tomorr.com", UserType.EMAIL);
        var artistId = "artist1";
        when(artistCache.containsArtistId(artistId)).thenReturn(false);
        when(artistService.exists(artistId)).thenReturn(false);
        when(artistSearchService.exists(artistId)).thenReturn(true);
        assertTrue(defaultFollowService.follow(user, artistId));
        verify(userService, times(1)).associate(user, artistId);
    }

    @Test
    public void follow_WhenArtistIsNotInCacheAndDatabaseAndSpotify_DoNotAssociateAndReturnFalse() {
        var user = new User("artist1@tomorr.com", UserType.EMAIL);
        var artistId = "artist1";
        when(artistCache.containsArtistId(artistId)).thenReturn(false);
        when(artistService.exists(artistId)).thenReturn(false);
        when(artistSearchService.exists(artistId)).thenReturn(false);
        assertFalse(defaultFollowService.follow(user, artistId));
        verify(userService, never()).associate(user, artistId);
    }
}