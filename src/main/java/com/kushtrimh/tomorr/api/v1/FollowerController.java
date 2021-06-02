package com.kushtrimh.tomorr.api.v1;

import com.kushtrimh.tomorr.api.v1.request.FollowRequest;
import com.kushtrimh.tomorr.user.User;
import com.kushtrimh.tomorr.user.UserType;
import com.kushtrimh.tomorr.user.service.UserService;
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
public class FollowerController {

    private final UserService userService;

    public FollowerController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/v1/follow")
    public ResponseEntity<?> follow(@RequestBody @Valid FollowRequest followRequest) {
        User user = new User(followRequest.getUser(), UserType.EMAIL);
        userService.follow(user, followRequest.getArtistId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
