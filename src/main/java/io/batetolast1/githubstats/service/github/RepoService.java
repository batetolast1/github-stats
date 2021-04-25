package io.batetolast1.githubstats.service.github;

import io.batetolast1.githubstats.model.github.Repo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RepoService {

    Page<Repo> getRepos(String username, Pageable pageable);
}
