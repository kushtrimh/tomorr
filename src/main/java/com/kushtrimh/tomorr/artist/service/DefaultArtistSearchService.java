package com.kushtrimh.tomorr.artist.service;

import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.artist.cache.ArtistCache;
import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.spotify.SpotifyApiException;
import com.kushtrimh.tomorr.spotify.TooManyRequestsException;
import com.kushtrimh.tomorr.spotify.api.SpotifyApiClient;
import com.kushtrimh.tomorr.spotify.api.request.artist.GetArtistApiRequest;
import com.kushtrimh.tomorr.spotify.api.request.artist.SearchApiRequest;
import com.kushtrimh.tomorr.spotify.api.response.ImageResponseData;
import com.kushtrimh.tomorr.spotify.api.response.SearchApiResponse;
import com.kushtrimh.tomorr.spotify.api.response.artist.ArtistResponseData;
import com.kushtrimh.tomorr.spotify.api.response.artist.GetArtistApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Kushtrim Hajrizi
 */
@Service
public class DefaultArtistSearchService implements ArtistSearchService {
    private final Logger logger = LoggerFactory.getLogger(DefaultArtistSearchService.class);

    private final ArtistCache artistCache;
    private final ArtistService artistService;
    private final SpotifyApiClient spotifyApiClient;

    public DefaultArtistSearchService(ArtistCache artistCache,
                                      ArtistService artistService,
                                      SpotifyApiClient spotifyApiClient) {
        this.artistCache = artistCache;
        this.artistService = artistService;
        this.spotifyApiClient = spotifyApiClient;
    }

    @Override
    public boolean exists(String artistId) {
        var request = new GetArtistApiRequest.Builder(artistId).build();
        try {
            GetArtistApiResponse response = spotifyApiClient.get(LimitType.SPOTIFY_SEARCH, request);
            artistCache.putArtistIds(List.of(response.getArtistResponseData().getId()));
            return true;
        } catch (TooManyRequestsException | SpotifyApiException e) {
            logger.debug("Could not check if artists exists", e);
            return false;
        }
    }

    @Override
    @Cacheable("artistsSearch")
    public List<Artist> search(String name, boolean external) {
        Objects.requireNonNull(name);
        List<Artist> artists = artistService.searchByName(name);
        if (!external) {
            return artists;
        }
        SearchApiRequest searchApiRequest = new SearchApiRequest.Builder(name)
                .limit(50)
                .offset(0)
                .types(List.of("artist"))
                .build();
        try {
            SearchApiResponse response = spotifyApiClient.get(LimitType.SPOTIFY_SEARCH, searchApiRequest);
            List<Artist> artistsFromExternalQuery = response.getArtists().getItems().stream()
                    .map(this::toArtist)
                    .collect(Collectors.toCollection(ArrayList::new));
            artistsFromExternalQuery.addAll(artists);
            return artistsFromExternalQuery.stream().distinct().toList();
        } catch (TooManyRequestsException | SpotifyApiException e) {
            logger.warn("Could not search for artists externally", e);
        }
        return artists;
    }

    private Artist toArtist(ArtistResponseData artistResponseData) {
        Objects.requireNonNull(artistResponseData);

        String imageHref = artistResponseData.getImages().stream().findFirst().map(ImageResponseData::getUrl).orElse("");
        return new Artist(artistResponseData.getId(),
                artistResponseData.getName(),
                imageHref,
                artistResponseData.getPopularity());
    }
}
