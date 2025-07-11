package tech.buildrun.springsecurity.repository;

import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.buildrun.springsecurity.entities.Role;
import tech.buildrun.springsecurity.entities.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
