package io.batetolast1.githubstats.service;

import io.batetolast1.githubstats.model.github.Repo;
import io.batetolast1.githubstats.repository.github.RepoRepository;
import io.batetolast1.githubstats.service.github.UserDetailsService;
import io.batetolast1.githubstats.service.github.impl.RepoServiceImpl;
import io.batetolast1.githubstats.util.RepoCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;

import static io.batetolast1.githubstats.util.RepoCreator.createValidRepo;
import static io.batetolast1.githubstats.util.UserDetailsCreator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(SpringExtension.class)
@DisplayName("RepoServiceImpl test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RepoServiceImplTest {

    @InjectMocks
    private RepoServiceImpl repoServiceImpl;

    @Mock
    private RepoRepository repoRepositoryMock;
    @Mock
    private UserDetailsService userDetailsServiceMock;

    @BeforeEach
    void setup() {
        BDDMockito.doNothing()
                .when(userDetailsServiceMock).fetchOrRefreshDataIfNeeded(VALID_USERNAME);
        BDDMockito.doThrow(new HttpClientErrorException(NOT_FOUND))
                .when(userDetailsServiceMock).fetchOrRefreshDataIfNeeded(INVALID_USERNAME);

        BDDMockito.when(repoRepositoryMock.findByUsername(ArgumentMatchers.eq(VALID_USERNAME), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(createValidRepo())));
        BDDMockito.when(repoRepositoryMock.findByUsername(ArgumentMatchers.eq(USERNAME_NOT_IN_DB), ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
    }

    @Test
    void should_Return_ListOfReposInsidePageObject_When_GetRepos() {
        String expectedRepoName = RepoCreator.createValidRepo().getName();

        Page<Repo> repos = repoServiceImpl.getRepos(VALID_USERNAME, PageRequest.of(0, 5));

        assertThat(repos).isNotNull();
        assertThat(repos.toList()).isNotEmpty();
        assertThat(repos.toList().get(0).getName()).isEqualTo(expectedRepoName);
    }

    @Test
    void should_Return_EmptyPage_When_GetRepos() {
        Page<Repo> repos = repoServiceImpl.getRepos(USERNAME_NOT_IN_DB, PageRequest.of(0, 5));

        assertThat(repos).isNotNull().isEmpty();
    }

    @Test
    void should_Throw_Exception_When_GetRepos() {
        assertThatExceptionOfType(HttpClientErrorException.class)
                .isThrownBy(() -> repoServiceImpl.getRepos(INVALID_USERNAME, PageRequest.of(0, 5)));
    }
}
