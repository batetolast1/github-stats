package io.batetolast1.githubstats.service.github.impl;

import io.batetolast1.githubstats.config.property.JdbcProperties;
import io.batetolast1.githubstats.model.github.Repo;
import io.batetolast1.githubstats.model.github.UserDetails;
import io.batetolast1.githubstats.repository.github.RepoRepository;
import io.batetolast1.githubstats.repository.github.UserDetailsRepository;
import io.batetolast1.githubstats.service.github.ApiService;
import io.batetolast1.githubstats.service.github.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final JdbcProperties jdbcProperties;
    private final ApiService apiService;
    private final RepoRepository repoRepository;
    private final UserDetailsRepository userDetailsRepository;

    @Override
    public void fetchOrRefreshDataIfNeeded(String username) {
        Optional<UserDetails> optionalUserDetails = userDetailsRepository.findByUsername(username);

        if (optionalUserDetails.isEmpty()) {
            saveNewUserDetails(username, fetchNewRepos(username));
        } else if (needToFetchNewData(optionalUserDetails.get())) {
            deleteOldRepos(username);

            updateUserDetails(optionalUserDetails.get(), fetchNewRepos(username));
        }
    }

    private boolean needToFetchNewData(UserDetails userDetails) {
        return Duration.between(userDetails.getLatestFetch(), LocalDateTime.now())
                .getSeconds() > jdbcProperties.getUpdateThreshold();
    }

    private void deleteOldRepos(String username) {
        repoRepository.deleteAllByUsername(username);
    }

    private List<Repo> fetchNewRepos(String username) {
        return repoRepository.saveAll(apiService.getAllRepos(username));
    }

    private void updateUserDetails(UserDetails userDetails, List<Repo> repos) {
        userDetails.setLatestFetch(LocalDateTime.now());
        userDetails.setAllStargazersCount(repos.stream()
                .mapToInt(Repo::getStargazersCount)
                .sum());
        userDetails.setRepos(repos);

        userDetailsRepository.save(userDetails);
    }

    private void saveNewUserDetails(String username, List<Repo> repos) {
        userDetailsRepository.save(UserDetails.builder()
                .username(username)
                .allStargazersCount(repos.stream()
                        .mapToInt(Repo::getStargazersCount)
                        .sum())
                .latestFetch(LocalDateTime.now())
                .repos(repos)
                .build());
    }

    @Override
    public UserDetails getBasicUserDetails(String username) {
        fetchOrRefreshDataIfNeeded(username);

        return userDetailsRepository.findByUsername(username)
                .orElseThrow(() -> new HttpServerErrorException(NOT_FOUND));
    }

    public UserDetails getUserDetailsWithAllRepos(String username) {
        fetchOrRefreshDataIfNeeded(username);

        return userDetailsRepository.findAllByUsername(username)
                .orElseThrow(() -> new HttpServerErrorException(NOT_FOUND));
    }
}
