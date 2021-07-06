package com.kushtrimh.tomorr.api.v1;

import com.kushtrimh.tomorr.api.v1.request.FollowRequest;
import com.kushtrimh.tomorr.follow.service.FollowService;
import com.kushtrimh.tomorr.user.User;
import com.kushtrimh.tomorr.user.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Kushtrim Hajrizi
 */
@ExtendWith(value = {SpringExtension.class})
public class FollowControllerTest {

    @Mock
    private FollowService followService;

    private MockMvc mockMvc;

    private final ObjectMapper mapper;

    public FollowControllerTest() {
        this.mapper = new ObjectMapper();
    }

    @BeforeEach
    public void init() {
        FollowController followerController = new FollowController(followService);
        mockMvc = MockMvcBuilders.standaloneSetup(followerController).build();
    }

    @Test
    public void follow_WhenRequestBodyIsEmpty_ReturnBadRequest() throws Exception {
        mockMvc.perform(post("/v1/follow"))
                .andExpect(status().isBadRequest());
        verify(followService, times(0))
                .follow(any(User.class), any(String.class));
    }

    @Test
    public void follow_WhenUserIsMissing_ReturnBadRequest() throws Exception {
        FollowRequest followRequest = new FollowRequest();
        followRequest.setArtistId("artist-id");
        mockMvc.perform(postWith("/v1/follow", followRequest))
                .andExpect(status().isBadRequest());
        verify(followService, times(0))
                .follow(any(User.class), any(String.class));
    }

    @Test
    public void follow_WhenArtistIsMissing_ReturnBadRequest() throws Exception {
        FollowRequest followRequest = new FollowRequest();
        followRequest.setUser("user-id");
        mockMvc.perform(postWith("/v1/follow", followRequest))
                .andExpect(status().isBadRequest());
        verify(followService, times(0))
                .follow(any(User.class), any(String.class));
    }

    @Test
    public void follow_WhenParametersAreValid_SaveFollowingAndReturnOk() throws Exception {
        String userId = "user-id";
        String artistId = "artist-id";

        FollowRequest followRequest = new FollowRequest();
        followRequest.setArtistId(artistId);
        followRequest.setUser(userId);

        User expectedUser = new User(userId, UserType.EMAIL);
        mockMvc.perform(postWith("/v1/follow", followRequest))
                .andExpect(status().isOk());
        verify(followService, times(1))
                .follow(expectedUser, artistId);
    }

    private MockHttpServletRequestBuilder postWith(String url, Object data) throws JsonProcessingException {
        return post(url)
            .content(mapper.writeValueAsString(data))
            .contentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
