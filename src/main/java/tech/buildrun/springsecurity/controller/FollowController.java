package tech.buildrun.springsecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import tech.buildrun.springsecurity.controller.dto.FollowResponseDto;
import tech.buildrun.springsecurity.controller.dto.FollowUserDto;
import tech.buildrun.springsecurity.entities.Follow;
import tech.buildrun.springsecurity.repository.FollowRepository;
import tech.buildrun.springsecurity.repository.UserRepository;
import tech.buildrun.springsecurity.service.FollowService;

import java.util.List;
import java.util.UUID;

@RestController
public class FollowController {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final FollowService followService;

    public FollowController(FollowRepository followRepository, UserRepository userRepository, FollowService followService) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.followService = followService;
    }

    @PostMapping("/follows/me")
    public ResponseEntity<Void> followUser(@RequestBody FollowUserDto dto,
                                           JwtAuthenticationToken token) {
        var follower = userRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(RuntimeException::new);

        var followed = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (follower.getUserId().equals(followed.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        if (followRepository.existsByFollowerAndFollowed(follower, followed)) {
            return ResponseEntity.status(409).build();
        }

        var follow = new Follow();
        follow.setFollowed(followed);
        follow.setFollower(follower);
        followRepository.save(follow);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/follows")
    public ResponseEntity<List<FollowResponseDto>> listFollows(@RequestParam String username,
                                                               JwtAuthenticationToken token) {
        var profile = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var follows = followRepository.findByFollowerUserId(profile.getUserId());

        List<FollowResponseDto> response = follows.stream()
                .map(f -> new FollowResponseDto(
                        f.getFollowed().getUserId(),
                        f.getFollowed().getUsername()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/follows/{username}")
    public ResponseEntity<Void> deleteFollow(@PathVariable String username,
            JwtAuthenticationToken token) {
        var follower = userRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(RuntimeException::new);
        var followed = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("You are not following this user"));

        followService.unfollow(follower, followed);

        return ResponseEntity.ok().build();
    }
}
