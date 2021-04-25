package io.batetolast1.githubstats.util;

import io.batetolast1.githubstats.model.github.Repo;

import static io.batetolast1.githubstats.util.UserDetailsCreator.VALID_USERNAME;

public class RepoCreator {

    public static final String VALID_NAME = "valid repo name";
    public static final Integer VALID_STARGAZERS_COUNT = 10;

    public static Repo createValidRepo() {
        return Repo.builder()
                .id(1L)
                .name(VALID_NAME)
                .username(VALID_USERNAME)
                .stargazersCount(VALID_STARGAZERS_COUNT)
                .build();
    }

    public static Repo createValidRepoToBeSaved() {
        return Repo.builder()
                .name(VALID_NAME)
                .username(VALID_USERNAME)
                .stargazersCount(VALID_STARGAZERS_COUNT)
                .build();
    }

    public static Repo createInvalidRepoToBeSaved() {
        return Repo.builder()
                .name(null)
                .username(null)
                .stargazersCount(null)
                .build();
    }
}
