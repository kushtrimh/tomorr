package com.kushtrimh.tomorr.api.v1;

import com.kushtrimh.tomorr.artist.Artist;
import com.kushtrimh.tomorr.artist.service.ArtistSearchService;
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

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        ArtistController controller = new ArtistController(artistSearchService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void search_WhenReturnedDataIsEmpty_ReturnEmptyResponse() throws Exception {
        var artistName = "artist-name";
        String response = mockMvc.perform(get("/v1/artist/search")
                        .param("name", artistName))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"artists\":[]}", response);
    }

    @Test
    public void search_WhenArtistsAreFound_ReturnArtistsResponse() throws Exception {
        List<Artist> artists = List.of(
                new Artist("artist1", "Artist One", null, 0),
                new Artist("artist2", "Artist Two", null, 0)
                );

        var artistName = "artist-name";
        when(artistSearchService.search(artistName, false)).thenReturn(artists);
        String response = mockMvc.perform(get("/v1/artist/search")
                        .param("name", artistName))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals("{\"artists\":[{\"id\":\"artist1\",\"name\":\"Artist One\"},{\"id\":\"artist2\",\"name\":\"Artist Two\"}]}",
                response);
    }
}