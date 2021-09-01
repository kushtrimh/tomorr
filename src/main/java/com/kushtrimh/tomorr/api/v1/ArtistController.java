package com.kushtrimh.tomorr.api.v1;

import com.kushtrimh.tomorr.api.v1.response.artist.ArtistResponseData;
import com.kushtrimh.tomorr.api.v1.response.artist.ArtistSearchResponse;
import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.artist.service.ArtistSearchService;
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

    // TODO: Add tests for this class

    private final ArtistSearchService artistSearchService;

    public ArtistController(ArtistSearchService artistSearchService) {
        this.artistSearchService = artistSearchService;
    }

    @GetMapping("/search")
    private ResponseEntity<ArtistSearchResponse> search(@RequestParam String name,
                                                        @RequestParam(required = false, defaultValue = "false") boolean external) {
        List<Artist> artists = artistSearchService.search(name, external);
        List<ArtistResponseData> artistsResponseData = artists.stream()
                .map(artist -> new ArtistResponseData(artist.id(), artist.name())).toList();
        return new ResponseEntity<>(new ArtistSearchResponse(artistsResponseData), HttpStatus.OK);
    }
}
