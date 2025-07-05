package tech.buildrun.springsecurity.controller.dto;


import java.time.Instant;
public record FeedItemDto(Long tweetId, String content, String username, Instant creation) {
}
