package io.batetolast1.githubstats.service.github;

import io.batetolast1.githubstats.model.github.Repo;

import java.util.List;

public interface ApiService {

    List<Repo> getAllRepos(String username);
}
