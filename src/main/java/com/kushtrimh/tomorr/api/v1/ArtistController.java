package com.kushtrimh.tomorr.api.v1;

import com.kushtrimh.tomorr.api.v1.response.artist.ArtistResponseData;
import com.kushtrimh.tomorr.api.v1.response.artist.ArtistSearchResponse;
import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.artist.service.ArtistSearchService;
import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.limit.RequestLimitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Kushtrim Hajrizi
 */
@RestController
@RequestMapping("/v1/artist")
public class ArtistController {

    private final ArtistSearchService artistSearchService;
    private final RequestLimitService requestLimitService;

    public ArtistController(ArtistSearchService artistSearchService, RequestLimitService requestLimitService) {
        this.artistSearchService = artistSearchService;
        this.requestLimitService = requestLimitService;
    }

    @GetMapping("/search")
    private ResponseEntity<ArtistSearchResponse> search(@RequestParam String name,
                                                        @RequestParam(required = false, defaultValue = "false") boolean external) {
        if (!requestLimitService.tryFor(LimitType.ARTIST_SEARCH)) {
            return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        }
        List<Artist> artists = artistSearchService.search(name, external);
        List<ArtistResponseData> artistsResponseData = artists.stream()
                .map(artist -> new ArtistResponseData(artist.id(), artist.name())).toList();
        return new ResponseEntity<>(new ArtistSearchResponse(artistsResponseData), HttpStatus.OK);
    }
}
