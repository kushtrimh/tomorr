package com.kushtrimh.tomorr.api.v1;

import com.kushtrimh.tomorr.api.v1.request.FollowRequest;
import com.kushtrimh.tomorr.follow.service.FollowService;
import com.kushtrimh.tomorr.user.User;
import com.kushtrimh.tomorr.user.UserType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Kushtrim Hajrizi
 */
@RestController
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/v1/follow")
    public ResponseEntity<?> follow(@RequestBody @Valid FollowRequest followRequest) {
        User user = new User(followRequest.getUser(), UserType.EMAIL);
        boolean followed = followService.follow(user, followRequest.getArtistId());
        if (!followed) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
