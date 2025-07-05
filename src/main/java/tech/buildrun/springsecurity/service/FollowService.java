package tech.buildrun.springsecurity.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tech.buildrun.springsecurity.entities.User;
import tech.buildrun.springsecurity.repository.FollowRepository;

@Service
public class FollowService {
    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    @Transactional
    public void unfollow(User follower, User followed) {
        followRepository.deleteByFollowerAndFollowed(follower, followed);
    }
}

