package io.batetolast1.githubstats.repository.github;

import io.batetolast1.githubstats.model.github.UserDetails;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    Optional<UserDetails> findByUsername(String username);

    @EntityGraph(value = "graph.UserDetails.repos")
    Optional<UserDetails> findAllByUsername(String username);
}
