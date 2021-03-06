package com.kushtrimh.tomorr.api.v1;

import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.artist.service.ArtistSearchService;
import com.kushtrimh.tomorr.limit.LimitType;
import com.kushtrimh.tomorr.limit.RequestLimitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith(value = {SpringExtension.class})
public class ArtistControllerTest {

    @Mock
    private ArtistSearchService artistSearchService;
    @Mock
    private RequestLimitService requestLimitService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        ArtistController controller = new ArtistController(artistSearchService, requestLimitService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void search_WhenRequestLimitIsExceeded_ReturnTooManyRequestsCode() throws Exception {
        var artistName = " artist-name";
        when(requestLimitService.cantSendRequest(LimitType.SPOTIFY_SEARCH)).thenReturn(true);
        mockMvc.perform(get("/api/v1/artist/search")
                        .param("name", artistName))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    public void search_WhenReturnedDataIsEmpty_ReturnEmptyResponse() throws Exception {
        var artistName = "artist-name";
        when(requestLimitService.tryFor(LimitType.SPOTIFY_SEARCH)).thenReturn(true);
        String response = mockMvc.perform(get("/api/v1/artist/search")
                        .param("name", artistName))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"artists\":[]}", response);
    }

    @Test
    public void search_WhenArtistsAreFoundWithExternalSearchDisabled_ReturnArtistsResponse() throws Exception {
        var artistName = "artist-name";
        when(requestLimitService.tryFor(LimitType.SPOTIFY_SEARCH)).thenReturn(true);
        assertArtistSearchSuccessfulResponse(artistName, false);
    }

    @Test
    public void search_WhenArtistsAreFoundWithExternalSearchEnabled_ReturnArtistsResponse() throws Exception {
        var artistName = "artist-name";
        when(requestLimitService.tryFor(LimitType.SPOTIFY_SEARCH)).thenReturn(true);
        assertArtistSearchSuccessfulResponse(artistName, true);
    }

    private void assertArtistSearchSuccessfulResponse(String artistName, boolean external) throws Exception {
        List<Artist> artists = List.of(
                new Artist("artist1", "Artist One", null, 0),
                new Artist("artist2", "Artist Two", null, 0)
        );

        when(artistSearchService.search(artistName, external)).thenReturn(artists);
        String response = mockMvc.perform(get("/api/v1/artist/search")
                        .param("name", artistName)
                        .param("external", String.valueOf(external)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"artists\":[{\"id\":\"artist1\",\"name\":\"Artist One\"},{\"id\":\"artist2\",\"name\":\"Artist Two\"}]}",
                response);
    }
}