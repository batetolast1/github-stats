package io.batetolast1.githubstats.repository.github;

import io.batetolast1.githubstats.model.github.Repo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoRepository extends JpaRepository<Repo, Long> {

    Page<Repo> findByUsername(String username, Pageable pageable);

    void deleteAllByUsername(String username);
}
