package io.batetolast1.githubstats.repository;

import io.batetolast1.githubstats.model.github.Repo;
import io.batetolast1.githubstats.repository.github.RepoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static io.batetolast1.githubstats.util.RepoCreator.createInvalidRepoToBeSaved;
import static io.batetolast1.githubstats.util.RepoCreator.createValidRepoToBeSaved;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
@DisplayName("Repo JPA Repository test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RepoRepositoryTest {

    @Autowired
    private RepoRepository repoRepository;

    @Test
    void should_PersistRepos_When_SaveAll() {
        List<Repo> reposToBeSaved = List.of(createValidRepoToBeSaved());

        List<Repo> savedRepos = repoRepository.saveAll(reposToBeSaved);

        assertThat(savedRepos).isNotNull().isNotEmpty().hasSize(1);
        assertThat(savedRepos.get(0).getId()).isNotNull();
    }

    @Test
    void should_ThrowConstraintViolationException_When_RepoIsInvalid() {
        List<Repo> repos = List.of(createInvalidRepoToBeSaved());

        repoRepository.saveAll(repos);

        assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> repoRepository.flush());
    }

    @Test
    void should_DeletePersistedRepos_When_DeleteAllByUsername() {
        List<Repo> repos = List.of(createValidRepoToBeSaved());

        repoRepository.saveAll(repos);

        repoRepository.deleteAllByUsername(repos.get(0).getUsername());

        Optional<Repo> optionalRepo = repoRepository.findById(repos.get(0).getId());

        assertThat(optionalRepo).isNotPresent();
    }
}