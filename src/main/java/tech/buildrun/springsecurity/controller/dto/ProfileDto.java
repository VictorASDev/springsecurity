package tech.buildrun.springsecurity.controller.dto;

import tech.buildrun.springsecurity.entities.Follow;
import tech.buildrun.springsecurity.entities.Tweet;

import java.util.List;

public record ProfileDto(String username,
                         List<FollowResponseDto> followers,
                         List<FollowResponseDto> following,
                         List<Tweet> posts) {}
