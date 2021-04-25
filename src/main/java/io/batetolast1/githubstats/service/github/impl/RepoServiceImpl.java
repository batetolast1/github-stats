package io.batetolast1.githubstats.service.github.impl;

import io.batetolast1.githubstats.model.github.Repo;
import io.batetolast1.githubstats.repository.github.RepoRepository;
import io.batetolast1.githubstats.service.github.RepoService;
import io.batetolast1.githubstats.service.github.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepoServiceImpl implements RepoService {

    private final RepoRepository repoRepository;
    private final UserDetailsService userDetailsService;

    @Override
    public Page<Repo> getRepos(String username, Pageable pageable) {
        userDetailsService.fetchOrRefreshDataIfNeeded(username);

        return repoRepository.findByUsername(username, pageable);
    }
}
