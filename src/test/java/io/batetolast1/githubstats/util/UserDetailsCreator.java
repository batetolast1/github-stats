package io.batetolast1.githubstats.util;

import io.batetolast1.githubstats.model.github.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

import static io.batetolast1.githubstats.util.RepoCreator.createValidRepo;

public class UserDetailsCreator {

    public static final String VALID_USERNAME = "validUsername";
    public static final String INVALID_USERNAME = "invalidUsername";
    public static final String USERNAME_NOT_IN_DB = "usernameNotInDb";
    public static final Integer VALID_STARGAZERS_COUNT = 100;

    public static UserDetails createValidUserDetails() {
        return UserDetails.builder()
                .id(1L)
                .username(VALID_USERNAME)
                .allStargazersCount(VALID_STARGAZERS_COUNT)
                .latestFetch(LocalDateTime.now())
                .repos(List.of(createValidRepo()))
                .build();
    }

    public static UserDetails createValidUserDetailsToBeSaved() {
        return UserDetails.builder()
                .username(VALID_USERNAME)
                .allStargazersCount(VALID_STARGAZERS_COUNT)
                .latestFetch(LocalDateTime.now())
                .build();
    }

    public static UserDetails createInvalidUserDetailsToBeSaved() {
        return UserDetails.builder()
                .username(null)
                .allStargazersCount(null)
                .latestFetch(null)
                .repos(null)
                .build();
    }
}
